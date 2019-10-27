package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;

import java.util.List;

public interface EpisodeInfraService {

    List<BotApiMethod> createFilm(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto);

    List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update);

    List<BotApiMethod> chooseSerial(CollectionDto collectionDto, UserDto userDto, Update update);

    List<BotApiMethod> chooseFilm(CollectionDto collectionDto, UserDto userDto, Update update);

}
