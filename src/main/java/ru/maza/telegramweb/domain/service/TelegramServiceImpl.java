package ru.maza.telegramweb.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.maza.telegramweb.dto.TranslateOptionDto;
import ru.maza.telegramweb.dto.buttons.Button;
import ru.maza.telegramweb.dto.callbackData.CallbackData;
import ru.maza.telegramweb.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TelegramServiceImpl implements TelegramService {

    private final Parser<TranslateOptionDto> parser;
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
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getFrom();
        } else {
            return update.getMessage().getFrom();
        }
    }

    @Override
    public Long getChatId(Update update) {
        if (update.getCallbackQuery() != null) {
            return update.getCallbackQuery().getMessage().getChatId();
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
    public void addReplyKeyboardButtons(SendMessage sendMessage, String... buttonNames) {
        sendMessage.setReplyMarkup(getReplyKeyboardMarkup(buttonNames));
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
        editMessageText.setParseMode("Markdown");
        editMessageText.setReplyMarkup(message.getReplyMarkup());
        return editMessageText;
    }


    @Override
    public void setForceReplyKeyboard(SendMessage sendMessage) {
        ForceReplyKeyboard forwardMessage = new ForceReplyKeyboard();
        forwardMessage.setSelective(true);
        sendMessage.setReplyMarkup(forwardMessage);
    }

    @Override
    public void addInlineKeyboardButtonMarkup(SendMessage sendMessage, Integer id, String... buttonNames) {
        sendMessage.setReplyMarkup(getKeyboardMarkup(id, buttonNames));
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
    public InlineKeyboardMarkup getKeyboardMarkup1(TranslateOptionDto translateOptionDto, Map<Integer, String> buttonNames) {
        InlineKeyboardMarkup replyMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardButtonsList = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtonsList.add(keyboardButtons);
        for (Map.Entry<Integer, String> entry : buttonNames.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(entry.getValue());
//            translateOptionDto.setWordId(entry.getKey());
            button.setCallbackData(parser.toJson(translateOptionDto));
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
            button.setCallbackData(callbackDataParser.toJson(callbackData));
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

}
