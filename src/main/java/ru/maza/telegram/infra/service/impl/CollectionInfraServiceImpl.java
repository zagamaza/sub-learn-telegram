package ru.maza.telegram.infra.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.CollectionClientApi;
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.domain.service.CollectionService;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.infra.service.CollectionInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionInfraServiceImpl implements CollectionInfraService {

    private final CollectionClientApi collectionClientApi;
    private final CollectionService collectionService;
    private final EpisodeInfraService episodeInfraService;
    private final BotService botService;

    @Override
    public List<BotApiMethod> getAllCollection(UserDto userDto, Update update) {
        RestPageImpl<CollectionCondensedDto> collections = collectionClientApi.getCollectionByUserId(
                userDto.getId(),
                PageRequest.of(0, 10, Sort.by("id"))
        );
        Integer collectionCount = (int)collections.getTotalElements();
        Page page = new Page(collectionCount, 0);
        return collectionService.getWindowAllCollection(page, collections.getContent(), userDto, update);
    }

    @Override
    public List<BotApiMethod> createCollection(UserDto userDto, Update update) {
        CollectionRequest collectionRequest = collectionService.fillCollectionRequest(update, userDto);
        CollectionDto collectionDto = collectionClientApi.create(collectionRequest);
        return collectionService.fillMessageCollectionCreated(collectionDto, update);
    }

    @Override
    public List<BotApiMethod> wantCreatePersonalCollection(UserDto userDto, Update update) {
        return botService.fillMessageHowYouAddCollection(update);
    }

    @Override
    public List<BotApiMethod> setIsSerialCollection(ChooseIsSerialCD chooseIsSerialCD, Update update, UserDto userDto) {
        collectionClientApi.updateIsSerial(
                chooseIsSerialCD.getCltnId(),
                chooseIsSerialCD.getIsSerial()
        );
        if (!chooseIsSerialCD.getIsSerial()) {
            return episodeInfraService.createFilm(chooseIsSerialCD, update, userDto);
        }
        return episodeInfraService.createSerial(chooseIsSerialCD.getCltnId(), update, userDto);
    }

    @Override
    public List<BotApiMethod> chooseCollection(Long collectionId, UserDto userDto, Update update) {
        CollectionDto collectionDto = collectionClientApi.get(collectionId);
//        if (isEmpty(collectionDto.getEpisodeDtos())) {
//            return collectionService.getMessageDeleteCollection(collectionDto, update);
//        }
        if (!collectionDto.isSerial()) {
            return episodeInfraService.chooseFilm(collectionDto, userDto, update);
        }
        return episodeInfraService.chooseSerial(collectionDto, userDto, update);
    }

    @Override
    public List<BotApiMethod> wantCreateCollection(UserDto userDto, Update update) {
        return collectionService.fillMessageHowYouAddCollection(update);
    }

    @Override
    public List<BotApiMethod> addCollection(UserDto userDto, Long collectionId, Update update) {
        try {
            collectionClientApi.copy(collectionId, userDto.getId());
        } catch (FeignException e) {
            return botService.getExceptionMessage(update);
        }
        return chooseCollection(collectionId, userDto, update);
    }

    @Override
    public List<BotApiMethod> deleteCollection(UserDto userDto, Long collectionId, Update update) {
        collectionClientApi.deleteLinkUserToCollection(collectionId, userDto.getId());
        return getAllCollection(userDto, update);
    }

    @Override
    public List<BotApiMethod> getCollectionByPage(UserDto userDto, PageCD pageCD, Update update) {
        RestPageImpl<CollectionCondensedDto> collections = collectionClientApi.getCollectionByUserId(
                userDto.getId(),
                PageRequest.of(pageCD.getPage(), 10, Sort.by("id"))
        );
        Integer collectionCount = (int)collections.getTotalElements();
        Page page = new Page(collectionCount, pageCD.getPage());
        return collectionService.getWindowAllCollection(page, collections.getContent(), userDto, update);
    }

}
