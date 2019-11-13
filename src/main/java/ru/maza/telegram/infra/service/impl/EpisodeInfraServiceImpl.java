package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.TrialClientApi;
import ru.maza.telegram.client.impl.CollectionClient;
import ru.maza.telegram.client.impl.EpisodeClient;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.domain.service.EpisodeService;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.Constant;
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

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class EpisodeInfraServiceImpl implements EpisodeInfraService {

    private final EpisodeClient episodeClient;
    private final TrialClientApi trialClientApi;
    private final CollectionClient collectionClient;
    private final EpisodeService episodeService;
    private final CommandInfraService commandInfraService;

    @Override
    public List<BotApiMethod> createFilm(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto) {
        EpisodeRequest episodeRequest = new EpisodeRequest(null, null, chooseIsSerialCD.getCltnId(), 0, 0);
        EpisodeDto episodeDto = episodeClient.create(episodeRequest);
        commandInfraService.save(new Command(userDto.getId(), Constant.ADD_FILE, episodeDto.getId()));
        return episodeService.getMessageForGetFile(update);
    }

    @Override
    public List<BotApiMethod> wantToCreateSeries(Long episodeId, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClient.get(episodeId);
        commandInfraService.save(new Command(userDto.getId(), Constant.ADD_SERIAL, episodeDto.getId()));
        return episodeService.getMessageForSetSerial(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update) {
        return episodeService.afterSaveSub(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> chooseSerial(CollectionDto collectionDto, UserDto userDto, Update update) {
        List<Integer> seasons = episodeClient.getSeasonsByCollectionId(collectionDto.getId());
        return episodeService.getMessageChooseSerial(seasons, collectionDto, update);
    }

    @Override
    public List<BotApiMethod> chooseFilm(CollectionDto collectionDto, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClient.getAllByCollectionId(
                collectionDto.getId(),
                PageRequest.of(0, 1)
        ).getContent().get(0);
        episodeDto.setLearnedPercent(episodeClient.getLearnedPercent(episodeDto.getId(), userDto.getId()));
        TrialDto trialDto = trialClientApi.getLastNotFinishTrialByEpisodeIdAndUserId(
                userDto.getId(),
                episodeDto.getId()
        );
        return episodeService.getMessageChooseFilm(collectionDto, episodeDto, trialDto, update);

    }

    @Override
    public List<BotApiMethod> chooseSeason(ChooseSeasonCD chooseSeasonCD, UserDto userDto, Update update) {
        CollectionDto collectionDto = collectionClient.get(chooseSeasonCD.getClnId());
        RestPageImpl<EpisodeDto> episodes = episodeClient
                .getByCollectionIdAndSeason(
                        chooseSeasonCD.getClnId(),
                        chooseSeasonCD.getSeason(),
                        PageRequest.of(0, 10, Sort.by("episode"))
                );
        episodes.stream().forEach(e -> e.setCollectionDto(collectionDto));
        Integer episodeCount = (int)episodes.getTotalElements();
        Page page = new Page(episodeCount, 0);
        return episodeService.getMessageChooseSeason(page, episodes.getContent(), update);
    }

    @Override
    public List<BotApiMethod> chooseSeries(ChsSeriesCD chsSeriesCD, UserDto userDto, Update update) {
        EpisodeDto episodeDto = episodeClient.get(chsSeriesCD.getEpdId());
        episodeDto.setCollectionDto(collectionClient.get(chsSeriesCD.getClnId()));
        episodeDto.setLearnedPercent(episodeClient.getLearnedPercent(episodeDto.getId(), userDto.getId()));
        TrialDto trialDto = trialClientApi.getLastNotFinishTrialByEpisodeIdAndUserId(
                userDto.getId(),
                episodeDto.getId()
        );
        return episodeService.getMessageChooseSeries(episodeDto, trialDto, update);
    }

    @Override
    public List<BotApiMethod> getSerialsByPage(UserDto userDto, PageSeriesCD pageSeriesCD, Update update) {
        RestPageImpl<EpisodeDto> episodes = episodeClient.getByCollectionIdAndSeason(
                pageSeriesCD.getClnId(),
                pageSeriesCD.getSn(),
                PageRequest.of(pageSeriesCD.getPg(), 10, Sort.by("episode"))
        );
        Integer episodeCount = (int)episodes.getTotalElements();
        Page page = new Page(episodeCount, pageSeriesCD.getPg());
        return episodeService.getMessageChooseSeason(page, episodes.getContent(), update);
    }

    @Override
    public List<BotApiMethod> createSerial(Long collectionId, Update update, UserDto userDto) {
        commandInfraService.save(new Command(userDto.getId(), Constant.ADD_SEASON, collectionId));
        return episodeService.getMessageForSetSeason(update);
    }

    @Override
    public List<BotApiMethod> addSeason(Long collectionId, UserDto userDto, Update update) {
        Integer season = Integer.parseInt(update.getMessage().getText());
        RestPageImpl<EpisodeDto> episodes = episodeClient.getByCollectionIdAndSeason(
                collectionId,
                season,
                PageRequest.of(0, 1, Sort.by("episode"))
        );
        if (!isEmpty(episodes.getContent())) {
            commandInfraService.save(new Command(userDto.getId(), Constant.ADD_SEASON, collectionId));
            return episodeService.getAlertSeasonAlreadyExists(episodes.getContent().get(0), update);
        } else {
            EpisodeRequest episodeRequest = new EpisodeRequest(null, null, collectionId, season, 0);
            EpisodeDto episodeDto = episodeClient.create(episodeRequest);
            commandInfraService.save(new Command(userDto.getId(), Constant.ADD_SERIAL, episodeDto.getId()));
            return episodeService.getMessageForSetSerial(episodeDto, update);
        }
    }

    @Override
    public List<BotApiMethod> addSeries(Long episodeId, UserDto userDto, Update update) {
        Integer series = Integer.parseInt(update.getMessage().getText());
        EpisodeDto episodeDto = episodeClient.get(episodeId);
        deleteTempEpisode(episodeDto);
        EpisodeDto episode = episodeClient.getByCollectionIdAndSeasonAndSeries(
                episodeDto.getCollectionDto().getId(),
                episodeDto.getSeason(),
                series
        );
        if (episode != null) {
            commandInfraService.save(new Command(userDto.getId(), Constant.ADD_SERIAL, episodeId));
            return episodeService.getAlertSeriesAlreadyExists(episode, update);
        } else {
            EpisodeRequest episodeRequest = new EpisodeRequest(
                    null,
                    null,
                    episodeDto.getCollectionDto().getId(),
                    episodeDto.getSeason(),
                    series
            );
            episodeDto = episodeClient.create(episodeRequest);
            commandInfraService.save(new Command(userDto.getId(), Constant.ADD_FILE, episodeDto.getId()));
            return episodeService.getMessageForGetFile(update);
        }

    }

    @Override
    public List<BotApiMethod> addFile(Long episodeId, UserDto userDto, Update update) {
        commandInfraService.save(new Command(userDto.getId(), Constant.ADD_FILE, episodeId));
        return episodeService.getMessageForGetFile(update);
    }

    @Override
    public List<BotApiMethod> deleteEpisode(UserDto userDto, Long episodeId, Update update) {
        EpisodeDto episodeDto = episodeClient.get(episodeId);
        episodeClient.delete(episodeId);
        return chooseSerial(episodeDto.getCollectionDto(), userDto, update);
    }

    private void deleteTempEpisode(EpisodeDto episodeDto) {
        if (episodeDto.getEpisode() == 0) {
            episodeClient.delete(episodeDto.getId());
        }
    }

}
