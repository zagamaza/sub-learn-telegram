package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.dto.callbackData.PageCD;

import java.util.List;

public interface CollectionInfraService {

    List<BotApiMethod> getAllCollection(UserDto userDto, Update update);

    List<BotApiMethod> createCollection(UserDto userDto, Update update);

    List<BotApiMethod> wantCreatePersonalCollection(UserDto userDto, Update update);

    List<BotApiMethod> setIsSerialCollection(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto);

    List<BotApiMethod> chooseCollection(Long collectionId, UserDto userDto, Update update);

    List<BotApiMethod> wantCreateCollection(UserDto userDto, Update update);

    List<BotApiMethod> addCollection(UserDto userDto, Long collectionId, Update update);

    List<BotApiMethod> deleteCollection(UserDto userDto, Long collectionId, Update update);

    List<BotApiMethod> getCollectionByPage(UserDto userDto, PageCD pageCD, Update update);

}
