package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.buttons.Button;

import java.util.List;

public interface TelegramService {

    Long getChatId(Update update);

    Integer getUserId(Update update);

    Message getMessage(Update update);

    User getUser(Update update);

    InlineKeyboardMarkup getKeyboardMarkup2(List<Button> buttons);

    DeleteMessage deleteMessage(Update update);

    void setForceReplyKeyboard(SendMessage sendMessage);

    InlineKeyboardMarkup getKeyboardMarkup(Integer id, String... buttonNames);

    InlineKeyboardMarkup getKeyboardMarkupWithInlineQuery(Integer id, String... buttonNames);

    EditMessageText getEditMessage(Update update);

    String getTextMessage(Update update);

    ReplyKeyboardMarkup getReplyKeyboardMarkup(String... buttonNames);

    BotApiMethod addAnswerCallbackQuery(CallbackQuery callbackQuery, boolean isShowAlert, String text);

    SendChatAction getSendChatAction(Message message, ActionType actionType);

    BotApiMethod fillEditMessage(Update update);

    SendMessage getSendMessage(Update update);

    EditMessageReplyMarkup deleteInlineKeyboard(Long chatId, Integer messageId);

    AnswerInlineQuery getAnswerInlineQuery(Update update);

    InlineQueryResultArticle fillInlineQueryResultPhoto(CollectionCondensedDto collectionCondensedDto);

}


