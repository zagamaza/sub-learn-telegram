package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.LeagueService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.Constant;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.PageButton;
import ru.maza.telegram.dto.buttons.competitions.AddFriendButton;
import ru.maza.telegram.dto.buttons.competitions.MyCompetitionsButton;
import ru.maza.telegram.dto.competition.CompetitionUserDto;
import ru.maza.telegram.dto.competition.LeagueDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.maza.telegram.utils.EmojiUtils.ALLOW_LEFT;
import static ru.maza.telegram.utils.EmojiUtils.ALLOW_RIGHT;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private final MessageSource messageSource;
    private final TelegramService telegramService;

    @Override
    public List<BotApiMethod> getMessageStartLeagues(
            Page page,
            UserDto userDto,
            List<LeagueDto> leagueDtos,
            Update update
    ) {

        List<Button> buttons = new ArrayList<>();
        buttons.add(new MyCompetitionsButton(getMessage("button.friends.competitions")));
        if (page.getPage() != 0) {
            buttons.add(new PageButton("league", page.getPage() - 1, true, ALLOW_LEFT, 2));
        }
        buttons.add(new CancelButton(getMessage("button.cancel.back"), Constant.START, 2));

        if ((page.getPage() + 1) * 10 < page.getCount()) {
            buttons.add(new PageButton("league", page.getPage() + 1, false, ALLOW_RIGHT, 1));
        }
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        fillEmojiToUserName(leagueDtos, page);
        String users = leagueDtos
                .stream()
                .map(u -> u.getUserName() + " - " + u.getExperience())
                .collect(Collectors.joining("\n\n"));
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setReplyMarkup(keyboardMarkup);
        editMessage.setParseMode(null);
        editMessage.setText(getMessage("league.start.window", users));
        return Collections.singletonList(editMessage);


    }

    private void fillEmojiToUserName(List<LeagueDto> leagueDtos, Page page) {
        for (int i = 0; i < leagueDtos.size(); i++) {
            LeagueDto league = leagueDtos.get(i);
            league.setUserName(league.getUserName().replace("_null", ""));
            if (page.getPage() == 0 && i == 0) {
                league.setUserName("🥇 @" + league.getUserName());
            } else if (page.getPage() == 0 && i == 1) {
                league.setUserName("🥈 @" + league.getUserName());
            } else if (page.getPage() == 0 && i == 2) {league.setUserName("🥉 @" + league.getUserName());} else {
                league.setUserName("@" + league.getUserName());
            }
        }
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
