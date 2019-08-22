package ru.maza.telegramweb.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.maza.telegramweb.domain.service.BotServiceImpl;
import ru.maza.telegramweb.domain.service.TelegramService;
import ru.maza.telegramweb.dto.CollectionCondensedDto;
import ru.maza.telegramweb.dto.TrialCondensedDto;
import ru.maza.telegramweb.dto.UserDto;
import ru.maza.telegramweb.dto.WordDto;
import ru.maza.telegramweb.infra.client.CollectionClientApi;
import ru.maza.telegramweb.infra.client.TranslatorClientApi;
import ru.maza.telegramweb.infra.client.TrialClientApi;
import ru.maza.telegramweb.infra.client.UserClientApi;
import ru.maza.telegramweb.infra.client.WordClientApi;

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

}
