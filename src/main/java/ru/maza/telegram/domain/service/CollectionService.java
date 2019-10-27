package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;

import java.util.List;

public interface CollectionService {

    List<BotApiMethod> getWindowAllCollection(
            Page page,
            List<CollectionCondensedDto> collections,
            UserDto userDto,
            Update update
    );

    CollectionRequest fillCollectionRequest(Update update, UserDto userDto);

    List<BotApiMethod> updateCollectionSerial(CollectionDto collectionDto, Update update);

    List<BotApiMethod> fillMessageHowYouAddCollection(Update update);

    List<BotApiMethod> fillMessageCollectionCreated(CollectionDto collectionDto, Update update);

    List<BotApiMethod> getAlertCopiedCollection(Update update, CollectionDto collection);

    List<BotApiMethod> getMessageDeleteCollection(CollectionDto collectionDto, Update update);

}
