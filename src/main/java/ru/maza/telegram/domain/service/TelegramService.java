package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.buttons.Button;

import java.util.List;
import java.util.Map;

public interface TelegramService {
    Long getChatId(Update update);

    Integer getUserId(Update update);

    Message getMessage(Update update);

    User getUser(Update update);

InlineKeyboardMarkup getKeyboardMarkup2(List<Button> buttons) ;

        InlineKeyboardMarkup getKeyboardMarkup1(TranslateOptionDto translateOptionDto, Map <Integer, String> buttonNames);

    DeleteMessage deleteMessage(Update update);

    EditMessageReplyMarkup deleteInlineKeyboard(Long chatId, Integer messageId);

    void addInlineKeyboardButtonMarkup(SendMessage sendMessage, Integer id, String... buttonNames);

    InlineKeyboardMarkup getKeyboardMarkup(Integer id, String... buttonNames);


    /**
     * Метод, который выставляет отправленное сообщение в виде replay
     *
     * @param sendMessage сообщение, которое планируеися отпровить
     */
    void setForceReplyKeyboard(SendMessage sendMessage);

    String getTextMessage(Update update);

    ReplyKeyboardMarkup getReplyKeyboardMarkup(String... buttonNames);

    void addReplyKeyboardButtons(SendMessage sendMessage, String... buttonNames);

    BotApiMethod addAnswerCallbackQuery(CallbackQuery callbackQuery, boolean isShowAlert, String text);

    SendChatAction getSendChatAction(Message message, ActionType actionType);

    BotApiMethod fillEditMessage(Update update);
}


