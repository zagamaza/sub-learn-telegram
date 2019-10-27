package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.TrialDto;

import java.util.List;

public interface EpisodeService {

    List<BotApiMethod> getMessageForGetFile(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> getMessageChooseSerial(List<Integer> seasons, CollectionDto collectionDto, Update update);

    List<BotApiMethod> getMessageChooseFilm(
            CollectionDto collectionDto,
            EpisodeDto episodeDto,
            TrialDto trialDto,
            Update update
    );

}
