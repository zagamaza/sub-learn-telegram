package ru.maza.telegram.domain.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdurmont.emoji.EmojiManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.CallbackService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.utils.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    @Deprecated
    public List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(telegramService.getChatId(update));
        editMessageText.setParseMode("Markdown");
        editMessageText.setText(getMessage(
                "translate.option.message",
                translateOptionDto.getTrialCondensedDto().getCollectionName(),
                EmojiUtils.extractEmojiPercent(translateOptionDto.getPresent()),
                EmojiUtils.extractEmojiPercent(translateOptionDto.getCorrectPercent()),
                translateOptionDto.getTranslatable().getWord()
        ));
        editMessageText.setMessageId(telegramService.getMessage(update).getMessageId());

        List<Button> collect = new ArrayList<>();

        collect.add(new CancelButton(
                getMessage("button.cancel"),
                new CancelCD(CancelCD.class.getSimpleName(), "/start"),
                1
        ));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(collect);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return List.of(editMessageText);
    }

    @Override
    public CallbackData getCallbackData(String data) {
        CallbackData callbackData = null;
        try {
            JsonNode jsonNode = objectMapper.readValue(data, JsonNode.class);
            Class clazz = Class.forName(CALLBACK_DATA_PACKAGE + jsonNode.get("cz").toString().replaceAll("\"", ""));
            callbackData = (CallbackData)objectMapper.convertValue(jsonNode, clazz);
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
        EditMessageText editMessageText = (EditMessageText)telegramService.fillEditMessage(update);

        editMessageText.setText(getMessage(
                "choose.collection.message",
                trialDto.getName(),
                trialDto.getPercent() + "%",
                trialDto.getCorrectPercent() + "%"
        ));
        List<Button> buttons = new ArrayList<>();

        buttons.add(new CancelButton(
                getMessage("button.cancel"),
                new CancelCD(CancelCD.class.getSimpleName(), "/start"),
                1
        ));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return Collections.singletonList(editMessageText);
    }


    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
