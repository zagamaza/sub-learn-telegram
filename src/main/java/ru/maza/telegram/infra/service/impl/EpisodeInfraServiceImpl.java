package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.EpisodeClientApi;
import ru.maza.telegram.client.TrialClientApi;
import ru.maza.telegram.domain.service.EpisodeService;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.EpisodeRequest;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.service.CommandInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeInfraServiceImpl implements EpisodeInfraService {

    private final EpisodeClientApi episodeClientApi;
    private final TrialClientApi trialClientApi;
    private final EpisodeService episodeService;
    private final CommandInfraService commandInfraService;

    @Override
    public List<BotApiMethod> createFilm(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto) {
        EpisodeRequest episodeRequest = new EpisodeRequest(null, null, chooseIsSerialCD.getCltnId(), 0, 0);
        EpisodeDto episodeDto = episodeClientApi.create(episodeRequest);
        commandInfraService.save(new Command(userDto.getId(), "/add_file", episodeDto.getId()));
        return episodeService.getMessageForGetFile(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update) {
        return episodeService.afterSaveSub(episodeDto, update);

    }

    @Override
    public List<BotApiMethod> chooseSerial(
            CollectionDto collectionDto,
            UserDto userDto,
            Update update
    ) {
        return episodeService.getMessageChooseSerial(collectionDto, update);
    }

    @Override
    public List<BotApiMethod> chooseFilm(
            CollectionDto collectionDto,
            UserDto userDto,
            Update update
    ) {
        EpisodeDto episodeDto = episodeClientApi.get(collectionDto.getEpisodeDtos().get(0).getId());
        episodeDto.setLearnedPercent(episodeClientApi.getLearnedPercent(episodeDto.getId(), userDto.getId()));
        TrialDto trialDto = trialClientApi.getLastNotFinishTrialByEpisodeIdAndUserId(
                userDto.getId(),
                episodeDto.getId()
        );
        return episodeService.getMessageChooseFilm(collectionDto, episodeDto, trialDto, update);

    }

}
