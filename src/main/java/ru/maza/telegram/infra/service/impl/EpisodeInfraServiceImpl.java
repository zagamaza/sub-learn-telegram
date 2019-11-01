package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.CollectionClientApi;
import ru.maza.telegram.client.EpisodeClientApi;
import ru.maza.telegram.client.TrialClientApi;
import ru.maza.telegram.domain.service.EpisodeService;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.EpisodeRequest;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.dto.callbackData.ChooseSeasonCD;
import ru.maza.telegram.dto.callbackData.ChsSeriesCD;
import ru.maza.telegram.dto.callbackData.PageSeriesCD;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.service.CommandInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class EpisodeInfraServiceImpl implements EpisodeInfraService {

    private final EpisodeClientApi episodeClientApi;
    private final TrialClientApi trialClientApi;
    private final CollectionClientApi collectionClientApi;
    private final EpisodeService episodeService;
    private final CommandInfraService commandInfraService;

    @Override
    public List<BotApiMethod> createFilm(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto) {
        EpisodeRequest episodeRequest = new EpisodeRequest(null, null, chooseIsSerialCD.getCltnId(), 0, 0);
        EpisodeDto episodeDto = episodeClientApi.create(episodeRequest);
        commandInfraService.save(new Command(userDto.getId(), "/add_file", episodeDto.getId()));
        return episodeService.getMessageForGetFile(update);
    }

    @Override
    public List<BotApiMethod> wantToCreateSeries(Long episodeId, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClientApi.get(episodeId);
        commandInfraService.save(new Command(userDto.getId(), "/add_serial", episodeDto.getId()));
        return episodeService.getMessageForSetSerial(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update) {
        return episodeService.afterSaveSub(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> chooseSerial(CollectionDto collectionDto, UserDto userDto, Update update) {
        List<Integer> seasons = episodeClientApi.getSeasonsByCollectionId(collectionDto.getId());
        return episodeService.getMessageChooseSerial(seasons, collectionDto, update);
    }

    @Override
    public List<BotApiMethod> chooseFilm(CollectionDto collectionDto, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClientApi.getAllByCollectionId(
                collectionDto.getId(),
                PageRequest.of(0, 1)
        ).get(0);
        episodeDto.setLearnedPercent(episodeClientApi.getLearnedPercent(episodeDto.getId(), userDto.getId()));
        TrialDto trialDto = trialClientApi.getLastNotFinishTrialByEpisodeIdAndUserId(
                userDto.getId(),
                episodeDto.getId()
        );
        return episodeService.getMessageChooseFilm(collectionDto, episodeDto, trialDto, update);

    }

    @Override
    public List<BotApiMethod> chooseSeason(ChooseSeasonCD chooseSeasonCD, UserDto userDto, Update update) {
        Integer episodeCount = episodeClientApi.getCountByCollectionIdAndSeason(
                chooseSeasonCD.getClnId(),
                chooseSeasonCD.getSeason()
        );
        CollectionDto collectionDto = collectionClientApi.get(chooseSeasonCD.getClnId());
        List<EpisodeDto> episodes = episodeClientApi
                .getByCollectionIdAndSeason(
                        chooseSeasonCD.getClnId(),
                        chooseSeasonCD.getSeason(),
                        PageRequest.of(0, 10, Sort.by("episode"))
                )
                .stream()
                .peek(e -> e.setCollectionDto(collectionDto))
                .collect(Collectors.toList());
        Page page = new Page(episodeCount, 0);
        return episodeService.getMessageChooseSeason(page, episodes, update);
    }

    @Override
    public List<BotApiMethod> chooseSeries(ChsSeriesCD chsSeriesCD, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClientApi.get(chsSeriesCD.getEpdId());
        episodeDto.setCollectionDto(collectionClientApi.get(chsSeriesCD.getClnId()));
        episodeDto.setLearnedPercent(episodeClientApi.getLearnedPercent(episodeDto.getId(), userDto.getId()));
        TrialDto trialDto = trialClientApi.getLastNotFinishTrialByEpisodeIdAndUserId(
                userDto.getId(),
                episodeDto.getId()
        );
        return episodeService.getMessageChooseSeries(episodeDto, trialDto, update);
    }

    @Override
    public List<BotApiMethod> getSerialsByPage(UserDto userDto, PageSeriesCD pageSeriesCD, Update update) {
        Integer episodeCount = episodeClientApi.getCountByCollectionIdAndSeason(
                pageSeriesCD.getClnId(),
                pageSeriesCD.getSn()
        );
        List<EpisodeDto> episodes = episodeClientApi.getByCollectionIdAndSeason(
                pageSeriesCD.getClnId(),
                pageSeriesCD.getSn(),
                PageRequest.of(pageSeriesCD.getPg(), 10, Sort.by("episode"))
        );
        Page page = new Page(episodeCount, pageSeriesCD.getPg());
        return episodeService.getMessageChooseSeason(page, episodes, update);
    }

    @Override
    public List<BotApiMethod> createSerial(Long collectionId, Update update, UserDto userDto) {
        commandInfraService.save(new Command(userDto.getId(), "/add_season", collectionId));
        return episodeService.getMessageForSetSeason(update);
    }

    @Override
    public List<BotApiMethod> addSeason(Long collectionId, UserDto userDto, Update update) {
        Integer season = Integer.parseInt(update.getMessage().getText());
        List<EpisodeDto> episodes = episodeClientApi.getByCollectionIdAndSeason(
                collectionId,
                season,
                PageRequest.of(0, 1, Sort.by("episode"))
        );
        if (!isEmpty(episodes)) {
            commandInfraService.save(new Command(userDto.getId(), "/add_season", collectionId));
            return episodeService.getAlertSeasonAlreadyExists(episodes.get(0), update);
        } else {
            EpisodeRequest episodeRequest = new EpisodeRequest(null, null, collectionId, season, 0);
            EpisodeDto episodeDto = episodeClientApi.create(episodeRequest);
            commandInfraService.save(new Command(userDto.getId(), "/add_serial", episodeDto.getId()));
            return episodeService.getMessageForSetSerial(episodeDto, update);
        }
    }

    @Override
    public List<BotApiMethod> addSeries(Long episodeId, UserDto userDto, Update update) {
        Integer series = Integer.parseInt(update.getMessage().getText());
        EpisodeDto episodeDto = episodeClientApi.get(episodeId);
        deleteTempEpisode(episodeDto);
        EpisodeDto episode = episodeClientApi.getByCollectionIdAndSeasonAndSeries(
                episodeDto.getCollectionDto().getId(),
                episodeDto.getSeason(),
                series
        );
        if (episode != null) {
            commandInfraService.save(new Command(userDto.getId(), "/add_serial", episodeId));
            return episodeService.getAlertSeriesAlreadyExists(episode, update);
        } else {
            EpisodeRequest episodeRequest = new EpisodeRequest(
                    null,
                    null,
                    episodeDto.getCollectionDto().getId(),
                    episodeDto.getSeason(),
                    series
            );
            episodeDto = episodeClientApi.create(episodeRequest);
            commandInfraService.save(new Command(userDto.getId(), "/add_file", episodeDto.getId()));
            return episodeService.getMessageForGetFile(update);
        }
    }

    @Override
    public List<BotApiMethod> addFile(Long episodeId, UserDto userDto, Update update) {
        commandInfraService.save(new Command(userDto.getId(), "/add_file", episodeId));
        return episodeService.getMessageForGetFile(update);
    }

    private void deleteTempEpisode(EpisodeDto episodeDto) {
        if (episodeDto.getEpisode() == 0) {
            episodeClientApi.delete(episodeDto.getId());
        }
    }

}
