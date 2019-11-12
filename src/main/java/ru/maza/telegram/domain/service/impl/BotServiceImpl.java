package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.MyCollectionsButton;
import ru.maza.telegram.dto.buttons.MySettingsButton;
import ru.maza.telegram.dto.buttons.MyTrialsButton;
import ru.maza.telegram.dto.buttons.SupportButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    public List<BotApiMethod> fillTranslateOptionDto(WordDto translate, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramService.getChatId(update));
        String ss = getMessage(
                "word.translate.message",
                translate.getWord(),
                translate.getTranscription(),
                translate.getTranslation().get(0).getTranslate().get(0)
        );
        sendMessage.setText(ss);
        return Collections.singletonList(sendMessage);
    }

    public List<BotApiMethod> fillMessageHowYouAddCollection(Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setText(getMessage("collection.send.name"));
        return List.of(editMessage);
    }

    @Override
    public List<BotApiMethod> getMessageForStart(Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(getMessage("start.window"));
        List<Button> buttons = new ArrayList<>();
        buttons.add(new MyCollectionsButton(getMessage("button.my.collections")));
        buttons.add(new MyTrialsButton(getMessage("button.my.trials")));
        buttons.add(new MySettingsButton(getMessage("button.my.settings")));
        sendMessage.setReplyMarkup(telegramService.getKeyboardMarkup2(buttons));
        return Collections.singletonList(sendMessage);
    }

    @Override
    public List<BotApiMethod> getEditMessageForStart(Update update) {
        EditMessageText editMessageText = telegramService.getEditMessage(update);
        editMessageText.setText(getMessage("start.window"));
        List<Button> buttons = new ArrayList<>();
        buttons.add(new MyCollectionsButton(getMessage("button.my.collections")));
        buttons.add(new MyTrialsButton(getMessage("button.my.trials")));
        buttons.add(new MySettingsButton(getMessage("button.my.settings")));
        editMessageText.setReplyMarkup(telegramService.getKeyboardMarkup2(buttons));
        return Collections.singletonList(editMessageText);
    }

    @Override
    public List<BotApiMethod> getExceptionMessage(Update update) {
        String errorText = getMessage("exception.message");
        BotApiMethod botApiMethod = telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, errorText);
        return Collections.singletonList(botApiMethod);
    }

    @Override
    @SneakyThrows
    public SendPhoto getMessageSupport(Integer supportId, String support, Update update) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(telegramService.getChatId(update));
        sendPhoto.setParseMode("Markdown");
        File file = ResourceUtils.getFile("classpath:support/" + support + ".jpg");
        sendPhoto.setCaption(getMessage("support." + support + ".screen"));
        sendPhoto.setPhoto(file);
        List<Button> buttons = new ArrayList<>();
        if (supportId != 1) {
            buttons.add(new SupportButton(supportId - 1, getMessage("button.support.back"), 2));
        }
        if (supportId != 14) {
            buttons.add(new SupportButton(supportId + 1, getMessage("button.support.next"), 2));
        }
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);

        sendPhoto.setReplyMarkup(keyboardMarkup);
        return sendPhoto;

    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}

