package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.CompetitionUserClient;
import ru.maza.telegram.client.LeagueClient;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.domain.service.LeagueService;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.dto.competition.CompetitionUserDto;
import ru.maza.telegram.dto.competition.LeagueDto;
import ru.maza.telegram.infra.service.LeagueInfraService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueInfraServiceImpl implements LeagueInfraService {

    private final LeagueService leagueService;
    private final LeagueClient leagueClient;
    private final CompetitionUserClient competitionUserClient;

    @Override
    public List<BotApiMethod> getLeagueUsersWindow(UserDto userDto, Update update) {
        CompetitionUserDto competitionUserDto = competitionUserClient.get(userDto.getId());
        RestPageImpl<LeagueDto> leagues = leagueClient.getByLeagueLevelCode(
                competitionUserDto.getLevel(),
                PageRequest.of(0, 10)
        );
        Integer collectionCount = (int)leagues.getTotalElements();
        Page page = new Page(collectionCount, 0);
        return leagueService.getMessageStartLeagues(page, userDto, leagues.getContent(), update);
    }

    @Override
    public List<BotApiMethod> getLeagueUsersByPage(UserDto userDto, PageCD pageCD, Update update) {
        CompetitionUserDto competitionUserDto = competitionUserClient.get(userDto.getId());
        RestPageImpl<LeagueDto> leagues = leagueClient.getByLeagueLevelCode(
                competitionUserDto.getLevel(),
                PageRequest.of(pageCD.getPage(), 10)
        );
        Integer leaguesCount = (int)leagues.getTotalElements();
        Page page = new Page(leaguesCount, pageCD.getPage());

        return leagueService.getMessageStartLeagues(page, userDto, leagues.getContent(), update);
    }

}
