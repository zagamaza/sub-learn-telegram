package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.PageCD;

import java.util.List;

public interface LeagueInfraService {

    List<BotApiMethod> getLeagueUsersWindow(UserDto userDto, Update update);

    List<BotApiMethod> getLeagueUsersByPage(UserDto userDto, PageCD pageCD, Update update);

}
