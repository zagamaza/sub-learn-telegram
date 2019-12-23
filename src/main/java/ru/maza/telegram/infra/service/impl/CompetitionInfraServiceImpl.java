package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.CompetitionUserClient;
import ru.maza.telegram.client.UserFriendClient;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.domain.service.CompetitionService;
import ru.maza.telegram.dto.Constant;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.dto.competition.CompetitionUserDto;
import ru.maza.telegram.dto.competition.UserFriendDto;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.service.CommandInfraService;
import ru.maza.telegram.infra.service.CompetitionInfraService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionInfraServiceImpl implements CompetitionInfraService {

    private final CompetitionService competitionService;
    private final CompetitionUserClient competitionUserClient;
    private final UserFriendClient userFriendClient;
    private final CommandInfraService commandInfraService;

    @Override
    public List<BotApiMethod> getCompetitionsWindow(UserDto userDto, Update update, Boolean isEdit) {
        RestPageImpl<CompetitionUserDto> users = competitionUserClient.getByUserFriendUserId(
                userDto.getId(),
                PageRequest.of(0, 10)
        );
        if (users.isEmpty()){
           return Collections.singletonList(competitionService.getAllertNotHaveRating(update));
        }
        Integer collectionCount = (int)users.getTotalElements();
        Page page = new Page(collectionCount, 0);
        return competitionService.getMessageStartCompetition(page, userDto, users.getContent(), update, isEdit);
    }

    @Override
    public List<BotApiMethod> wantAddFriend(UserDto userDto, Update update) {
        commandInfraService.save(new Command(userDto.getId(), Constant.ADD_FRIEND, null));
        return competitionService.getMessageWantAddFriend(update);
    }

    @Override
    public List<BotApiMethod> addFriend(UserDto userDto, Update update) {
        if (!update.getMessage().hasContact()) {
            return competitionService.getMessageNotValidContact(update);
        } else {
            Integer userID = update.getMessage().getContact().getUserID();
            try {
                CompetitionUserDto competitionUserDto = competitionUserClient.getByTelegramId(userID.longValue());
                userFriendClient.create(new UserFriendDto(userDto.getId(), competitionUserDto.getId()));
                return competitionService.getMessageAddedFriend(update);
            } catch (Exception e) {
                return competitionService.getMessageFriendAbsent(update);
            }
        }
    }

    @Override
    public List<BotApiMethod> getFriendsByPage(UserDto userDto, PageCD pageCD, Update update) {
        RestPageImpl<CompetitionUserDto> users = competitionUserClient.getByUserFriendUserId(
                userDto.getId(),
                PageRequest.of(pageCD.getPage(), 10)
        );
        Integer userCounts = (int)users.getTotalElements();
        Page page = new Page(userCounts, pageCD.getPage());
        return competitionService.getMessageStartCompetition(page, userDto, users.getContent(), update, true);
    }

    @Override
    public List<BotApiMethod> deleteFriend(UserDto userDto, Update update) {
        if (!update.getMessage().hasContact()) {
            return competitionService.getMessageNotValidContact(update);
        } else {
            Integer userID = update.getMessage().getContact().getUserID();
            try {
                CompetitionUserDto competitionFriendUserDto = competitionUserClient.getByTelegramId(userID.longValue());
                userFriendClient.delete(userDto.getId(), competitionFriendUserDto.getId());
                return competitionService.getMessageDeletedFriend(update);
            } catch (Exception e) {
                return competitionService.getMessageFriendAbsent(update);
            }
        }
    }

    @Override
    public List<BotApiMethod> wantDeleteFriend(UserDto userDto, Update update) {
        commandInfraService.save(new Command(userDto.getId(), Constant.DELETE_FRIEND, null));
        return competitionService.getMessageWantDeleteFriend(update);
    }

}