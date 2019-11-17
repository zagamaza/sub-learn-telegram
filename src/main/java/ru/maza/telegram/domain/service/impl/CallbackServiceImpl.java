package ru.maza.telegram.domain.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final TelegramService telegramService;
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;


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

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
