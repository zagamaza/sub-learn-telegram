package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.competition.CompetitionUserDto;

import java.util.List;

public interface CompetitionService {

    List<BotApiMethod> getMessageStartCompetition(
            Page page, UserDto userDto,
            List<CompetitionUserDto> competitionUsers,
            Update update,
            Boolean isEdit
    );

    List<BotApiMethod> getMessageWantAddFriend(Update update);

    List<BotApiMethod> getMessageAddedFriend(Update update);

    List<BotApiMethod> getMessageFriendAbsent(Update update);

    List<BotApiMethod> getMessageNotValidContact(Update update);

    BotApiMethod getAllertNotHaveRating(Update update);

    List<BotApiMethod> getMessageWantDeleteFriend(Update update);

    List<BotApiMethod> getMessageDeletedFriend(Update update);

}
