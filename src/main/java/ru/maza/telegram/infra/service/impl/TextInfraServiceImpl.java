package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.maza.telegram.client.TranslatorClientApi;
import ru.maza.telegram.client.UserClientApi;
import ru.maza.telegram.client.WordClientApi;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.domain.service.impl.BotServiceImpl;
import ru.maza.telegram.dto.Lang;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.UserRequest;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.infra.service.TextInfraService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TextInfraServiceImpl implements TextInfraService {

    private final TelegramService telegramService;
    private final BotServiceImpl botService;

    private final WordClientApi wordClientApi;
    private final UserClientApi userClientApi;
    private final TranslatorClientApi translatorClientApi;

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserDto saveUser(Update update, Long userId) {
        User user = telegramService.getUser(update);
        UserDto userDto = null;
        try {
            userDto = userClientApi.getByTelegramId(userId);
        } catch (Exception ignored) {}
        if (userDto == null) {
            userDto = userClientApi.save(UserRequest.from(user));
        }
        return userDto;
    }

    @Override
    public List<BotApiMethod> translateWord(String answer, Update update) {
        WordDto translate = translatorClientApi.translate(answer, Lang.EN_RU
        );
        wordClientApi.update(translate);
        return botService.fillTranslateOptionDto(translate, update);
    }

}
