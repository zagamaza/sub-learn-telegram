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
import ru.maza.telegram.dto.buttons.competitions.MyCompetitionsButton;
import ru.maza.telegram.dto.competition.LeagueDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.maza.telegram.utils.EmojiUtils.ALLOW_LEFT;
import static ru.maza.telegram.utils.EmojiUtils.ALLOW_RIGHT;
import static ru.maza.telegram.utils.EmojiUtils.extractEmojiPercent;

@Service
@RequiredArgsConstructor
public class LeagueServiceImpl implements LeagueService {

    private static final int SUNDAY = 7;

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
                .map(u -> u.getUserName() + " - " + extractEmojiPercent(u.getExperience()))
                .collect(Collectors.joining("\n\n"));
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setReplyMarkup(keyboardMarkup);
        editMessage.setParseMode(null);
        LocalDateTime localDateTime = LocalDateTime.now();
        int day = SUNDAY - localDateTime.getDayOfWeek().getValue();
        int hours = 23 - localDateTime.getHour();
        String date = extractEmojiPercent(day) + " д. " + extractEmojiPercent(hours) + " ч.";
        editMessage.setText(getMessage("league.start.window", date, users));
        return Collections.singletonList(editMessage);
    }

    private void fillEmojiToUserName(List<LeagueDto> leagueDtos, Page page) {
        for (int i = 0; i < leagueDtos.size(); i++) {
            LeagueDto league = leagueDtos.get(i);
            league.setUserName(league.getUserName().replace("_null", ""));
            int pageCount = page.getPage() * 10;
            if (i == 0) {
                if (page.getPage() == 0) {
                    league.setUserName("🥇 @" + league.getUserName());
                } else {
                    league.setUserName(pageCount + 1 + i + ". @" + league.getUserName());
                }
            } else if (i == 1) {
                if (pageCount == 0) {
                    league.setUserName("🥈 @" + league.getUserName());
                } else {
                    league.setUserName(pageCount + 1 + i + ". @" + league.getUserName());
                }
            } else if (i == 2) {
                if (pageCount == 0) {
                    league.setUserName("🥉 @" + league.getUserName());
                } else {
                    league.setUserName(pageCount + 1 + i + ". @" + league.getUserName());
                }
            } else {
                league.setUserName(pageCount + 1 + i + ". @" + league.getUserName());
            }
        }
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
