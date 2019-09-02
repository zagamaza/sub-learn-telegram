package ru.maza.telegram.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.maza.telegram.domain.service.BotServiceImpl;
import ru.maza.telegram.domain.service.CollectionService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.*;
import ru.maza.telegram.infra.client.*;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TextInfraServiceImpl implements TextInfraService {

    private final TelegramService telegramService;
    private final BotServiceImpl botService;
    private final TrialClientApi trialClientApi;
    private final CollectionClientApi collectionClientApi;
    private final WordClientApi wordClientApi;
    private final UserClientApi userClientApi;
    private final TranslatorClientApi translatorClientApi;
    private final CollectionService collectionService;

    @Override
    public List<BotApiMethod> getCollectionsByUserId(Update update) {

        Integer userId = telegramService.getUserId(update);
        List<CollectionCondensedDto> collectionCondensed = collectionClientApi.getCollectionByUserId(userId.longValue());
        return botService.fillMessageLastCollection(collectionCondensed, update);
    }

    @Override
    public List<BotApiMethod> getTrialsByUserId(Update update) {
        Long userId = telegramService.getUserId(update).longValue();
        List<TrialCondensedDto> lastConsedTrial = trialClientApi.getLastConsedTrial(userId);
        return botService.fillMessageLastCondensedTrial(lastConsedTrial, update);
    }

    @Override
    @Cacheable("users")
    public UserDto saveUser(Update update) {
        User user = update.getMessage().getFrom();
        UserDto userDto = new UserDto(user.getId().longValue(), user.getFirstName(), null, null);
        return userClientApi.save(userDto);
    }

    @Override
    public List<BotApiMethod> translateWord(String answer, Update update) {
        WordDto translate = translatorClientApi.translate(answer);
        wordClientApi.update(translate);
        return botService.fillTranslateOptionDto(translate, update);
    }

    @Override
    public List<BotApiMethod> createCollection(Update update) {
        return botService.fillMessageSetMeNameForCollection(update);
    }

    @Override
    public List<BotApiMethod> setCollectionName(Update update) {
        CollectionRequest collectionRequest = collectionService.fillCollectionRequest(update);
        CollectionDto collectionDto = collectionClientApi.create(collectionRequest);
        return botService.fillMessageCollectionCreated(collectionDto, update);


    }

}
