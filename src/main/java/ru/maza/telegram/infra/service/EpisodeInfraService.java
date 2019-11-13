package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.dto.callbackData.ChooseSeasonCD;
import ru.maza.telegram.dto.callbackData.ChsSeriesCD;
import ru.maza.telegram.dto.callbackData.PageSeriesCD;

import java.util.List;

public interface EpisodeInfraService {

    List<BotApiMethod> createFilm(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto);

    List<BotApiMethod> wantToCreateSeries(Long episodeId, UserDto userDto, Update update);

    List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> chooseSerial(CollectionDto collectionDto, UserDto userDto, Update update);

    List<BotApiMethod> chooseFilm(CollectionDto collectionDto, UserDto userDto, Update update);

    List<BotApiMethod> chooseSeason(ChooseSeasonCD chooseSeasonCD, UserDto userDto, Update update);

    List<BotApiMethod> chooseSeries(ChsSeriesCD chsSeriesCD, UserDto userDto, Update update);

    List<BotApiMethod> getSerialsByPage(UserDto userDto, PageSeriesCD pageSeriesCD, Update update);

    List<BotApiMethod> createSerial(Long collectionId, Update update, UserDto userDto);

    List<BotApiMethod> addSeason(Long commandId, UserDto userDto, Update update);

    List<BotApiMethod> addSeries(Long commandId, UserDto userDto, Update update);

    List<BotApiMethod> addFile(Long episodeId, UserDto userDto, Update update);

    List<BotApiMethod> deleteEpisode(UserDto userDto, Long episodeId, Update update);

}
