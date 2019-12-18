package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.CompetitionService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.Constant;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.PageButton;
import ru.maza.telegram.dto.buttons.competitions.AddFriendButton;
import ru.maza.telegram.dto.competition.CompetitionUserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.maza.telegram.utils.EmojiUtils.ALLOW_LEFT;
import static ru.maza.telegram.utils.EmojiUtils.ALLOW_RIGHT;

@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    @Override
    public List<BotApiMethod> getMessageStartCompetition(
            Page page, UserDto userDto,
            List<CompetitionUserDto> competitionUsers,
            Update update,
            Boolean isEdit
    ) {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new AddFriendButton(getMessage("button.competitions.want.add.friend"), 1));

        if (page.getPage() != 0) {
            buttons.add(new PageButton("friend", page.getPage() - 1, true, ALLOW_LEFT, 2));
        }
        buttons.add(new CancelButton(getMessage("button.cancel.back"), Constant.START, 2));

        if ((page.getPage() + 1) * 10 < page.getCount()) {
            buttons.add(new PageButton("friend", page.getPage() + 1, false, ALLOW_RIGHT, 1));
        }
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        fillEmojiToUserName(competitionUsers, page);
        String users = competitionUsers
                .stream()
                .map(u -> u.getUserName() + " - " + u.getExperience())
                .collect(Collectors.joining("\n\n"));
        if (isEdit) {
            EditMessageText editMessage = telegramService.getEditMessage(update);
            editMessage.setReplyMarkup(keyboardMarkup);
            editMessage.setText(getMessage("competitions.start.window", users));
            return Collections.singletonList(editMessage);

        } else {
            SendMessage sendMessage = telegramService.getSendMessage(update);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setText(getMessage("competitions.start.window", users));
            return Collections.singletonList(sendMessage);
        }
    }

    private void fillEmojiToUserName(List<CompetitionUserDto> competitionUsers, Page page) {
        for (int i = 0; i < competitionUsers.size(); i++) {
            CompetitionUserDto user = competitionUsers.get(i);
            user.setUserName(user.getUserName().replace("_null", ""));
            if (page.getPage() == 0 && i == 0) {user.setUserName("🥇 @" + user.getUserName());}
            else if (page.getPage() == 0 && i == 1) {user.setUserName("🥈 @" + user.getUserName());}
            else if (page.getPage() == 0 && i == 2) {user.setUserName("🥉 @" + user.getUserName());}
            else { user.setUserName("@" + user.getUserName()); }
        }
    }

    @Override
    public List<BotApiMethod> getMessageWantAddFriend(Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setText(getMessage("competitions.get.contact"));
        return Collections.singletonList(editMessage);
    }

    @Override
    public List<BotApiMethod> getMessageAddedFriend(Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        String firstName = update.getMessage().getContact().getFirstName();
        if (firstName.isEmpty()) {
            firstName = update.getMessage().getContact().getLastName();
        }
        sendMessage.setParseMode(null);
        sendMessage.setText(getMessage("competitions.added.friend", firstName));
        return Collections.singletonList(sendMessage);
    }

    @Override
    public List<BotApiMethod> getMessageFriendAbsent(Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        String firstName = update.getMessage().getContact().getFirstName();
        sendMessage.setParseMode(null);
        sendMessage.setText(getMessage("competitions.friend.absent", firstName));
        return Collections.singletonList(sendMessage);
    }

    @Override
    public List<BotApiMethod> getMessageNotValidContact(Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(getMessage("competitions.validation.not.valid.contact"));
        return Collections.singletonList(sendMessage);
    }

    @Override
    public BotApiMethod getAllertNotHaveRating(Update update) {
        String text = getMessage("competitions.validation.not.train");
        return telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, text);
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }


}
