package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TrialDto;

import java.util.List;

public interface EpisodeService {

    List<BotApiMethod> getMessageForGetFile(Update update);

    List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> getMessageChooseSerial(
            Page page,
            List<Integer> seasons,
            CollectionDto collectionDto,
            Update update
    );

    List<BotApiMethod> getMessageChooseFilm(
            CollectionDto collectionDto,
            EpisodeDto episodeDto,
            TrialDto trialDto,
            Update update
    );

    List<BotApiMethod> getMessageChooseSeason(Page page, List<EpisodeDto> episodes, Update update);

    List<BotApiMethod> getMessageChooseSeries(EpisodeDto episodeDto, TrialDto trialDto, Update update);

    List<BotApiMethod> getMessageForSetSeason(Update update);

    List<BotApiMethod> getMessageForSetSerial(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> getAlertSeasonAlreadyExists(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> getAlertSeriesAlreadyExists(EpisodeDto episode, Update update);

}
