package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.PageCD;

import java.util.List;

public interface CompetitionInfraService {

    List<BotApiMethod> getCompetitionsWindow(UserDto userDto, Update update, Boolean isEdit);

    List<BotApiMethod> wantAddFriend(UserDto userDto, Update update);

    List<BotApiMethod> addFriend(UserDto userDto, Update update);

    List<BotApiMethod> getFriendsByPage(UserDto userDto, PageCD pageCD, Update update);

}
