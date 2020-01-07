package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
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
import ru.maza.telegram.dto.buttons.competitions.DeleteFriendButton;
import ru.maza.telegram.dto.buttons.competitions.MyLeagueButton;
import ru.maza.telegram.dto.competition.CompetitionUserDto;
import ru.maza.telegram.utils.EmojiUtils;

import java.io.InputStream;
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
        buttons.add(new MyLeagueButton(getMessage("button.common.league")));
        buttons.add(new DeleteFriendButton(getMessage("button.competitions.want.delete.friend"), 2));
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
                .map(u -> u.getUserName() + " - " + EmojiUtils.extractEmojiPercent(u.getExperience()))
                .collect(Collectors.joining("\n\n"));
        if (isEdit) {
            EditMessageText editMessage     = telegramService.getEditMessage(update);
            editMessage.setReplyMarkup(keyboardMarkup);
            editMessage.setParseMode(null);
            editMessage.setText(getMessage("competitions.start.window", users));
            return Collections.singletonList(editMessage);

        } else {
            SendMessage sendMessage = telegramService.getSendMessage(update);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setParseMode(null);
            sendMessage.setText(getMessage("competitions.start.window", users));
            return Collections.singletonList(sendMessage);
        }
    }

    private void fillEmojiToUserName(List<CompetitionUserDto> competitionUsers, Page page) {
        for (int i = 0; i < competitionUsers.size(); i++) {
            CompetitionUserDto user = competitionUsers.get(i);
            user.setUserName(user.getUserName().replace("_null", ""));
            int pageCount = page.getPage() * 10;
            if (i == 0) {
                if (page.getPage() == 0) {
                    user.setUserName("🥇 @" + user.getUserName());
                } else {
                    user.setUserName(pageCount + 1 + i + ". @" + user.getUserName());
                }
            } else if (i == 1) {
                if (pageCount == 0) {
                    user.setUserName("🥈 @" + user.getUserName());
                } else {
                    user.setUserName(pageCount + 1 + i + ". @" + user.getUserName());
                }
            } else if (i == 2) {
                if (pageCount == 0) {
                    user.setUserName("🥉 @" + user.getUserName());
                } else {
                    user.setUserName(pageCount + 1 + i + ". @" + user.getUserName());
                }
            } else {
                user.setUserName(pageCount + 1 + i + ". @" + user.getUserName());
            }

        }
    }

    @Override
    public SendMediaGroup getMessageWantAddFriend(Update update) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(telegramService.getChatId(update));
        InputStream contact = this.getClass().getResourceAsStream("/support/add_contact.jpg");
        InputStream clip = this.getClass().getResourceAsStream("/support/clip.jpg");
        InputMediaPhoto inputMediaContact = new InputMediaPhoto();
        inputMediaContact.setCaption(getMessage("competitions.get.contact"));
        inputMediaContact.setMedia(contact, "photo");
        InputMediaPhoto inputMediaClip = new InputMediaPhoto();
        inputMediaClip.setMedia(clip, "photo1");
        sendMediaGroup.setMedia(List.of(inputMediaClip, inputMediaContact));
        return sendMediaGroup;
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

    @Override
    public SendMediaGroup getMessageWantDeleteFriend(Update update) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(telegramService.getChatId(update));
        InputStream contact = this.getClass().getResourceAsStream("/support/add_contact.jpg");
        InputStream clip = this.getClass().getResourceAsStream("/support/clip.jpg");
        InputMediaPhoto inputMediaContact = new InputMediaPhoto();
        inputMediaContact.setCaption(getMessage("competitions.get.contact.delete"));
        inputMediaContact.setMedia(contact, "photo");
        InputMediaPhoto inputMediaClip = new InputMediaPhoto();
        inputMediaClip.setMedia(clip, "photo1");
        sendMediaGroup.setMedia(List.of(inputMediaClip, inputMediaContact));
        return sendMediaGroup;
    }

    @Override
    public List<BotApiMethod> getMessageDeletedFriend(Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        String firstName = update.getMessage().getContact().getFirstName();
        if (firstName.isEmpty()) {
            firstName = update.getMessage().getContact().getLastName();
        }
        sendMessage.setParseMode(null);
        sendMessage.setText(getMessage("competitions.deleted.friend", firstName));
        return Collections.singletonList(sendMessage);
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }


}
