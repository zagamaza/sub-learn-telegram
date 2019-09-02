package ru.maza.telegram.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.ChooseCollectionButton;
import ru.maza.telegram.dto.buttons.ChooseTranslateButton;
import ru.maza.telegram.dto.buttons.ChooseTrialButton;
import ru.maza.telegram.dto.buttons.TranscriptionButton;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegram.dto.callbackData.ChooseTranslateCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.TranscriptionCD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallbackServiceImpl implements CallbackService {

    private static final String CALLBACK_DATA_PACKAGE = "ru.maza.telegram.dto.callbackData.";

    private static final String NOT = EmojiManager.getForAlias("x").getUnicode();
    private static final String OK = EmojiManager.getForAlias("white_check_mark").getUnicode();
    private static final String RIGHT = EmojiManager.getForAlias("ballot_box_with_check").getUnicode();
    private static final String TADA = EmojiManager.getForAlias("tada").getUnicode();

    private final TelegramService telegramService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;


    @Override
    public BotApiMethod checkTranslation(ChooseTranslateCD chooseTranslateCD, Update update) {
        EditMessageText editMessageText = (EditMessageText) telegramService.fillEditMessage(update);
        InlineKeyboardMarkup replyMarkup = editMessageText.getReplyMarkup();
        List<List<InlineKeyboardButton>> keyboard = replyMarkup.getKeyboard();

        keyboard.stream()
                .filter(inlineKeyboardButtons -> inlineKeyboardButtons.size() < 2)
                .map(inlineKeyboardButtons -> inlineKeyboardButtons.get(0))
                .forEach(inlineKeyboard -> {
                    ChooseTranslateCD chooseTranslate = null;
                    chooseTranslate = (ChooseTranslateCD) getCallbackData(inlineKeyboard.getCallbackData());
                    if (chooseTranslate.getRwId().equals(chooseTranslate.getWId())) {
                        if (chooseTranslate.getWId().equals(chooseTranslateCD.getWId())) {
                            inlineKeyboard.setText(OK + " " + inlineKeyboard.getText());
                        } else {
                            inlineKeyboard.setText(RIGHT + " " + inlineKeyboard.getText());
                        }
                    }
                    if (chooseTranslate.getWId().equals(chooseTranslateCD.getWId()) &&
                            !chooseTranslate.getWId().equals(chooseTranslate.getRwId())) {
                        inlineKeyboard.setText(NOT + inlineKeyboard.getText());
                    }
                });

        editMessageText.setReplyMarkup(replyMarkup);
        return editMessageText;
    }

    @Override
    public List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(telegramService.getChatId(update));
        editMessageText.setParseMode("Markdown");
        editMessageText.setText(getMessage(
                "translate.option.message",
                translateOptionDto.getTrialCondensedDto().getCollectionName(),
                extractEmojiPercent(translateOptionDto.getPresent()),
                extractEmojiPercent(translateOptionDto.getCorrectPercent()),
                translateOptionDto.getTranslatable().getWord()
        ));
        editMessageText.setMessageId(telegramService.getMessage(update).getMessageId());

        List<Button> collect =
                translateOptionDto.getTranslations().stream()
                        .map(wordDto -> new ChooseTranslateButton(
                                String.join(",", wordDto.getTranslation()),
                                new ChooseTranslateCD(
                                        ChooseTranslateCD.class.getSimpleName(),
                                        wordDto.getId(),
                                        translateOptionDto.getTrialCondensedDto().getId(),
                                        translateOptionDto.getTranslatable().getId()
                                ),
                                wordDto.getId(),
                                translateOptionDto.getTrialCondensedDto().getId(),
                                translateOptionDto
                                        .getTranslatable()
                                        .getId()
                                        .equals(wordDto.getId()),
                                1
                        )).collect(Collectors.toList());
        Collections.shuffle(collect);
        collect.add(new TranscriptionButton(
                getMessage("transcription.button"),
                new TranscriptionCD(
                        TranscriptionCD.class.getSimpleName(),
                        translateOptionDto.getTranslatable().getId()
                ),
                2
        ));
        collect.add(new CancelButton(
                getMessage("cancel.button"),
                new CancelCD(CancelCD.class.getSimpleName(), true),
                1
        ));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(collect);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return List.of(editMessageText);
    }

    private String extractEmojiPercent(Integer percent) {
        StringBuilder presentInEmoji = new StringBuilder();
        String number = String.valueOf(percent);
        for (int i = 0; i < number.length(); i++) {
            int j = Character.digit(number.charAt(i), 10);
            presentInEmoji.append(j);
            presentInEmoji.append('\u20E3');
        }
        return presentInEmoji.toString();
    }

    @Override
    public CallbackData getCallbackData(String data) {
        CallbackData callbackData = null;
        try {
            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);
            Class clazz = Class.forName(CALLBACK_DATA_PACKAGE + jsonNode.get("clazz").toString().replaceAll("\"", ""));
            callbackData = (CallbackData) objectMapper.convertValue(jsonNode, clazz);
        } catch (Exception ignored) {
        }
        return callbackData;

    }

    @Override
    public List<BotApiMethod> fillAnswerAlertForWord(WordDto wordDto, Update update) {
        String transcription = getMessage("transcription.message", wordDto.getWord(), wordDto.getTranscription());
        return List.of(fillAnswerAlert(transcription, update));
    }

    private BotApiMethod fillAnswerAlert(String text, Update update) {
        return telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, text);
    }

    @Override
    public List<BotApiMethod> getStatisticByTrial(TrialDto trialDto, Update update) {
        EditMessageText editMessageText = (EditMessageText) telegramService.fillEditMessage(update);

        editMessageText.setText(getMessage(
                "choose.collection.message",
                trialDto.getCollectionDto().getName(),
                trialDto.getPercent() + "%",
                trialDto.getCorrectPercent() + "%"
        ));
        List<Button> buttons = new ArrayList<>();
        buttons.add(new ChooseTrialButton(
                "continue",
                new ChooseTrialCD(ChooseTrialCD.class.getSimpleName(), trialDto.getId(), true),
                2,
                trialDto.getId(),
                true
        ));
        buttons.add(new CancelButton(
                getMessage("cancel.button"),
                new CancelCD(CancelCD.class.getSimpleName(), true),
                1
        ));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return Collections.singletonList(editMessageText);
    }

    @Override
    public List<BotApiMethod> afterSaveCollection(CollectionDto collectionDto, Update update) {
        SendMessage sendMessage = new SendMessage(
                telegramService.getChatId(update),
                getMessage("save.collection.message", collectionDto.getName())
        );

        SendMessage sendMessage1 = new SendMessage(
                telegramService.getChatId(update),
                getMessage("want.trial.message")
        );

        List<Button> buttons = new ArrayList<>();
        buttons.add(new ChooseCollectionButton(
                collectionDto.getId(),
                "start",
                new ChooseCollectionCD(ChooseCollectionCD.class.getSimpleName(), collectionDto.getId()),
                2
        ));
        buttons.add(new CancelButton("cancel", new CancelCD(CancelCD.class.getSimpleName(), true), 1));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        sendMessage1.setReplyMarkup(keyboardMarkup);

        return List.of(sendMessage, sendMessage1);
    }

    @Override
    public List<BotApiMethod> finishTrial(TranslateOptionDto translateOptionDto, Update update) {
        DeleteMessage deleteMessage = telegramService.deleteMessage(update);
        String finishTrial = getMessage("success.trial.message", TADA, translateOptionDto.getCorrectPercent());
        return List.of(
                deleteMessage,
                telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, finishTrial)
        );
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
