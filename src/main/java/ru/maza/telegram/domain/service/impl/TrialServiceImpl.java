package ru.maza.telegram.domain.service.impl;

import com.vdurmont.emoji.EmojiManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.maza.telegram.domain.service.CallbackService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.domain.service.TrialService;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TranslationDto;
import ru.maza.telegram.dto.TrialCondensedDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.ChooseLearnedWordButton;
import ru.maza.telegram.dto.buttons.ChooseStartTrialButton;
import ru.maza.telegram.dto.buttons.ChooseTranslateButton;
import ru.maza.telegram.dto.buttons.ChooseTrialButton;
import ru.maza.telegram.dto.buttons.PageButton;
import ru.maza.telegram.dto.buttons.TranscriptionButton;
import ru.maza.telegram.dto.callbackData.CTlteCD;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.utils.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrialServiceImpl implements TrialService {

    private final TelegramService telegramService;
    private final CallbackService callbackService;
    private final MessageSource messageSource;

    private static final String LEFT = EmojiManager.getForAlias("arrow_left").getUnicode();
    private static final String NOT_LEFT = EmojiManager.getForAlias("arrow_right").getUnicode();
    private static final String NOT = EmojiManager.getForAlias("x").getUnicode();
    private static final String OK = EmojiManager.getForAlias("white_check_mark").getUnicode();
    private static final String RIGHT = EmojiManager.getForAlias("ballot_box_with_check").getUnicode();
    private static final String TADA = EmojiManager.getForAlias("tada").getUnicode();

    @Override
    public BotApiMethod checkTranslation(CTlteCD chooseTranslateCD, Update update) {
        EditMessageText editMessageText = (EditMessageText)telegramService.fillEditMessage(update);
        InlineKeyboardMarkup replyMarkup = editMessageText.getReplyMarkup();
        List<List<InlineKeyboardButton>> keyboard = replyMarkup.getKeyboard();

        keyboard.stream()
                .filter(inlineKeyboardButtons -> inlineKeyboardButtons.size() < 2)
                .filter(inlineKeyboardButtons -> !inlineKeyboardButtons.get(0).getText().equals("🔙 Назад"))
                .map(inlineKeyboardButtons -> inlineKeyboardButtons.get(0))
                .forEach(inlineKeyboard -> {
                    CTlteCD chooseTranslate = null;
                    chooseTranslate = (CTlteCD)callbackService.getCallbackData(inlineKeyboard.getCallbackData());
                    if (chooseTranslate.getRw().equals(chooseTranslate.getWd())) {
                        if (chooseTranslate.getRw().equals(chooseTranslateCD.getWd())) {
                            inlineKeyboard.setText(OK + " " + inlineKeyboard.getText());
                        } else {
                            inlineKeyboard.setText(RIGHT + " " + inlineKeyboard.getText());
                        }
                    }
                    if (chooseTranslate.getWd().equals(chooseTranslateCD.getWd()) &&
                            !chooseTranslate.getRw().equals(chooseTranslateCD.getWd())) {
                        inlineKeyboard.setText(NOT + inlineKeyboard.getText());
                    }
                });

        editMessageText.setReplyMarkup(replyMarkup);
        return editMessageText;
    }

    @Override
    public List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update) {
        EditMessageText editMessageText = telegramService.getEditMessage(update);
        WordDto translatableWord = translateOptionDto.getTranslatable();

        editMessageText.setText(getMessage(
                "translate.option.message",
                translateOptionDto.getTrialCondensedDto().getCollectionName(),
                EmojiUtils.extractEmojiPercent(translateOptionDto.getPresent()),
                EmojiUtils.extractEmojiPercent(translateOptionDto.getCorrectPercent()),
                translateOptionDto.getTranslatable().getWord().toUpperCase()
        ));

        List<Button> collect = translateOptionDto
                .getTranslations()
                .stream()
                .map(wordDto -> new ChooseTranslateButton(
                        wordDto.getTranslation().get(0).getTranslate().get(0),
                        translateOptionDto.getTrialWordId(),
                        translateOptionDto.getTrialCondensedDto().getId(),
                        wordDto.getId(),
                        translateOptionDto.getTranslatable().getId(),
                        1
                )).collect(Collectors.toList());
        WordDto translatable = translateOptionDto.getTranslatable();
        collect.add(new ChooseTranslateButton(
                translatable.getTranslation().get(0).getTranslate().get(0),
                translateOptionDto.getTrialWordId(),
                translateOptionDto.getTrialCondensedDto().getId(),
                translatable.getId(),
                translateOptionDto.getTranslatable().getId(),
                1
        ));

        Collections.shuffle(collect);

        collect.add(new TranscriptionButton(getMessage("transcription.button"), translatableWord.getId(), 2));
        collect.add(new ChooseLearnedWordButton(
                getMessage("button.word.know"),
                translateOptionDto.getTrialWordId(),
                translateOptionDto.getTrialCondensedDto().getId(),
                1
        ));
        collect.add(new CancelButton(getMessage("button.cancel.back"), "/my_collection", 1));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(collect);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return List.of(editMessageText);
    }


    @Override
    public List<BotApiMethod> finishTrial(TrialDto trialDto, Update update) {
        String finishTrial = getMessage("success.trial.message", TADA, trialDto.getCorrectPercent());
        return List.of(
                telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, finishTrial)
        );
    }

    @Override
    public List<BotApiMethod> fillMessageWithAllTrial(
            Page page,
            List<TrialCondensedDto> lastConsedTrial,
            Update update
    ) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        List<Button> cancelButtons = lastConsedTrial.stream()
                                                    .map(trialCondensedDto ->
                                                                 new ChooseTrialButton(
                                                                         trialCondensedDto.getCollectionName(),
                                                                         1,
                                                                         trialCondensedDto.getId()
                                                                 )
                                                    ).collect(Collectors.toList());

        editMessage.setText(getMessage("trial.my"));
        if (page.getPage() != 0) {
            cancelButtons.add(new PageButton("trial", page.getPage() - 1, true, LEFT, 2));
        }
        cancelButtons.add(new CancelButton(getMessage("button.cancel.back"), "/my_collection", 2));
        if ((page.getPage() + 1) * 10 < page.getCount()) {
            cancelButtons.add(new PageButton("trial", page.getPage() + 1, false, NOT_LEFT, 2));
        }
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(cancelButtons);
        editMessage.setReplyMarkup(keyboardMarkup);
        return Collections.singletonList(editMessage);
    }

    @Override
    public List<BotApiMethod> fillAlertStatisticByTrial(TrialDto trialDto, Update update) {
        String message = getMessage(
                "trial.one.result",
                trialDto.getName(),
                EmojiUtils.extractEmojiPercent(trialDto.getPercent()),
                EmojiUtils.extractEmojiPercent(trialDto.getCorrectPercent())
        );
        EditMessageText editMessageText = telegramService.getEditMessage(update);
        List<Button> buttons = List.of(
                ChooseStartTrialButton.from(
                        trialDto.getEpisodeDto().getId(),
                        trialDto.getId(),
                        getMessage("button.continue.trial"),
                        1
                ),
                new CancelButton(getMessage("button.cancel"), "/start", 1)
        );
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        editMessageText.setText(message);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return Collections.singletonList(editMessageText);
    }

    @Override
    public BotApiMethod getAlertWithAllTranslate(WordDto word, Update update) {
        Map<String, List<String>> partToTranslate = word.getTranslation()
                                                        .stream()
                                                        .limit(3)
                                                        .collect(Collectors.toMap(
                                                                TranslationDto::getPartSpeech,
                                                                translationDto -> translationDto
                                                                        .getTranslate()
                                                                        .stream()
                                                                        .limit(3)
                                                                        .collect(
                                                                                Collectors.toList())
                                                        ));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Варианты перевода для слова " + "\n👉🏼" + word.getWord().toUpperCase() + "\n\n");
        stringBuilder.append("🗣" + " [" + word.getTranscription() + "]" + "\n\n" + "📖");
        partToTranslate
                .forEach((key, value) -> stringBuilder.append(getMessage(
                        "alert.translate",
                        key,
                        String.join(", ", value)
                )));
        return telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, stringBuilder.toString());
    }

    @Override
    public List<BotApiMethod> fillMessageRepeatTrial(Long id, UserDto userDto, Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        List<Button> buttons = new ArrayList<>();
        buttons.add(ChooseStartTrialButton.from(id, null, getMessage("button.start.trial"), 1));
        buttons.add(new CancelButton(
                getMessage("button.cancel"),
                new CancelCD(CancelCD.class.getSimpleName(), "/my_collection"),
                1
        ));
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        editMessage.setReplyMarkup(keyboardMarkup);
        editMessage.setText(getMessage("trial.is.repeat"));
        return Collections.singletonList(editMessage);

    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
