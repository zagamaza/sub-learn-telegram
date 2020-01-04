package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.FoundCollection;
import ru.maza.telegram.dto.buttons.AddSearchCollectionsButton;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.utils.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private static final String MARKDOWN = "Markdown";
    private static final String EMPTY_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Kodak-Max-400-35mm-Film.jpg/255px-Kodak-Max-400-35mm-Film.jpg";


    private final MessageSource messageSource;
    private final Parser<CallbackData> callbackDataParser;

    @Override
    public Integer getUserId(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getFrom().getId();
        } else {
            return update.getMessage().getFrom().getId();
        }
    }

    @Override
    public Message getMessage(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getMessage();
        } else {
            return update.getMessage();
        }
    }

    @Override
    public User getUser(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        } else if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else {
            return update.getInlineQuery().getFrom();
        }
    }

    @Override
    public Long getChatId(Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getFrom().getId().longValue();
        } else if (update.hasInlineQuery()) {
            return update.getInlineQuery().getFrom().getId().longValue();
        } else {
            return update.getMessage().getChatId();
        }
    }

    @Override
    public String getTextMessage(Update update) {
        return update.getMessage().getText();
    }

    @Override
    public ReplyKeyboardMarkup getReplyKeyboardMarkup(String... buttonNames) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        IntStream.range(0, buttonNames.length).forEachOrdered(i -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            String buttonName = buttonNames[i];
            keyboardRow.add(buttonName);
            keyboardRowList.add(keyboardRow);
        });
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    @Override
    public AnswerCallbackQuery addAnswerCallbackQuery(
            CallbackQuery callbackQuery,
            boolean isShowAlert,
            String text
    ) {

        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setShowAlert(isShowAlert);
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    @Override
    public SendChatAction getSendChatAction(Message message, ActionType actionType) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(message.getChatId());
        sendChatAction.setAction(actionType);
        return sendChatAction;
    }

    @Override
    public BotApiMethod fillEditMessage(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(getChatId(update));
        Message message = update.getCallbackQuery().getMessage();
        editMessageText.setMessageId(message.getMessageId());
        editMessageText.setText(message.getText());
        editMessageText.setParseMode(MARKDOWN);
        editMessageText.setReplyMarkup(message.getReplyMarkup());
        return editMessageText;
    }

    @Override
    public SendMessage getSendMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(MARKDOWN);
        sendMessage.setChatId(getChatId(update));
        return sendMessage;
    }

    @Override
    public EditMessageText getEditMessage(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setParseMode(MARKDOWN);
        editMessageText.setChatId(getChatId(update));
        editMessageText.setMessageId(getMessage(update).getMessageId());
        return editMessageText;
    }


    @Override
    public void setForceReplyKeyboard(SendMessage sendMessage) {
        ForceReplyKeyboard forwardMessage = new ForceReplyKeyboard();
        forwardMessage.setSelective(true);
        sendMessage.setReplyMarkup(forwardMessage);
    }

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup(Integer id, String... buttonNames) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtonsList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtonsList.add(keyboardButtons);
        for (int i = 0; i < buttonNames.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonNames[i]);
            button.setCallbackData(id.toString());
            keyboardButtons.add(button);

            keyboardButtons = new ArrayList<>();
            keyboardButtonsList.add(keyboardButtons);
        }

        replyMarkup.setKeyboard(keyboardButtonsList);
        return replyMarkup;
    }

    @Override
    public InlineKeyboardMarkup getKeyboardMarkupWithInlineQuery(Integer id, String... buttonNames) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtonsList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtonsList.add(keyboardButtons);
        for (int i = 0; i < buttonNames.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonNames[i]);
            button.setCallbackData(id.toString());
            keyboardButtons.add(button);

            keyboardButtons = new ArrayList<>();
            keyboardButtonsList.add(keyboardButtons);
        }

        replyMarkup.setKeyboard(keyboardButtonsList);
        return replyMarkup;
    }

    @Override
    public InlineKeyboardMarkup getKeyboardMarkup2(List<Button> buttons) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtonsList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtonsList.add(keyboardButtons);

        for (Button but : buttons) {
            CallbackData callbackData = but.getCallbackData();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(but.getName());
            if (callbackData != null) {
                button.setCallbackData(callbackDataParser.toJson(callbackData));
            } else { button.setSwitchInlineQueryCurrentChat(""); }
            keyboardButtons.add(button);
            if (but.getCountButtonInLine() > 1) {
                continue;
            }
            keyboardButtons = new ArrayList<>();
            keyboardButtonsList.add(keyboardButtons);
        }

        replyMarkup.setKeyboard(keyboardButtonsList);
        return replyMarkup;
    }

    @Override
    public DeleteMessage deleteMessage(Update update) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(getChatId(update));
        deleteMessage.setMessageId(getMessage(update).getMessageId());
        return deleteMessage;
    }

    @Override
    public EditMessageReplyMarkup deleteInlineKeyboard(Long chatId, Integer messageId) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        return editMessageReplyMarkup;
    }

    @Override
    public AnswerInlineQuery getAnswerInlineQuery(Update update) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setPersonal(true);
        answerInlineQuery.setInlineQueryId(update.getInlineQuery().getId());
        return answerInlineQuery;
    }

    @Override
    public InlineQueryResultArticle fillInlineQueryResultPhoto(CollectionCondensedDto collectionCondensedDto) {
        InlineQueryResultArticle inlineQueryResultArticle = new InlineQueryResultArticle();
        inlineQueryResultArticle.setDescription("Рейтинг " + collectionCondensedDto.getRating());
        inlineQueryResultArticle.setHideUrl(true);
        inlineQueryResultArticle.setId(UUID.randomUUID().toString());
        inlineQueryResultArticle.setTitle(collectionCondensedDto.getName());
        String url = collectionCondensedDto.getUrl();
        if (url.startsWith("http")) {
            inlineQueryResultArticle.setThumbUrl(collectionCondensedDto.getUrl());
        } else {
            inlineQueryResultArticle.setThumbUrl(EMPTY_IMAGE);
        }
        inlineQueryResultArticle.setThumbUrl(collectionCondensedDto.getUrl());

        InlineKeyboardMarkup keyboardMarkup = getKeyboardMarkup2(List.of(
                new AddSearchCollectionsButton(
                        collectionCondensedDto.getId(),
                        null,
                        getMessage("button.add.collection"),
                        1
                )));
        inlineQueryResultArticle.setReplyMarkup(keyboardMarkup);

        InputTextMessageContent inputTextMessageContent = new InputTextMessageContent();
        inputTextMessageContent.setMessageText(collectionCondensedDto.getName());
        inlineQueryResultArticle.setInputMessageContent(inputTextMessageContent);
        return inlineQueryResultArticle;
    }

    @Override
    public InlineQueryResultArticle fillInlineQueryResultPhoto(FoundCollection foundCollection) {
        InlineQueryResultArticle inlineQueryResultArticle = new InlineQueryResultArticle();
        inlineQueryResultArticle.setDescription("year: " + foundCollection.getYear() +
                                                        "\n" + "type: " + foundCollection.getType());
        inlineQueryResultArticle.setHideUrl(true);
        inlineQueryResultArticle.setId(UUID.randomUUID().toString());
        inlineQueryResultArticle.setTitle(foundCollection.getTitle());
        String url = foundCollection.getPoster();
        if (url.startsWith("http")){
            inlineQueryResultArticle.setThumbUrl(foundCollection.getPoster());
        }else {
            inlineQueryResultArticle.setThumbUrl(EMPTY_IMAGE);
        }
        InlineKeyboardMarkup keyboardMarkup = getKeyboardMarkup2(List.of(
                new AddSearchCollectionsButton(
                        null,
                        foundCollection.getImdbID(),
                        getMessage("button.add.collection"),
                        1
                )));
        inlineQueryResultArticle.setReplyMarkup(keyboardMarkup);

        InputTextMessageContent inputTextMessageContent = new InputTextMessageContent();
        inputTextMessageContent.setMessageText(foundCollection.getTitle());
        inlineQueryResultArticle.setInputMessageContent(inputTextMessageContent);
        return inlineQueryResultArticle;

    }

    @Override
    public List<BotApiMethod> getTelegramMessage(Update update, InlineKeyboardMarkup keyboardMarkup, String text) {
        EditMessageText editMessage;
        SendMessage sendMessage;
        if (update.hasCallbackQuery()&& update.getCallbackQuery().getMessage() != null) {
            editMessage = getEditMessage(update);
            editMessage.setReplyMarkup(keyboardMarkup);
            editMessage.setText(text);
            return Collections.singletonList(editMessage);
        } else {
            sendMessage = getSendMessage(update);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setText(text);
            return Collections.singletonList(sendMessage);
        }
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
