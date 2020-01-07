package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.competition.LeagueDto;

import java.util.List;


public interface LeagueService {

    List<BotApiMethod> getMessageStartLeagues(Page page, UserDto userDto, List<LeagueDto> content, Update update);

}
