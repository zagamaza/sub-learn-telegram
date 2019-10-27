package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.UserSettingClientApi;
import ru.maza.telegram.domain.service.UserSettingService;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.UserSettingDto;
import ru.maza.telegram.dto.callbackData.TranslateCountCD;
import ru.maza.telegram.dto.callbackData.WordCountCD;
import ru.maza.telegram.infra.service.UserSettingInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSettingInfraServiceImpl implements UserSettingInfraService {

    private final UserSettingClientApi userSettingClientApi;
    private final UserSettingService userSettingService;

    @Override
    public List<BotApiMethod> getMySettings(UserDto userDto, Update update) {
        UserSettingDto userSettingDto = userSettingClientApi.getByUserId(userDto.getId());
        if (userSettingDto.getId() == null) {
            userSettingDto.setUserId(userDto.getId());
            userSettingDto = userSettingClientApi.create(userSettingDto);
        }
        return userSettingService.fillMessageWithAllSettings(userSettingDto, update);
    }

    @Override
    @CacheEvict(value = "users", key = "#userDto.telegramId")
    public List<BotApiMethod> updateSchedule(UserDto userDto, Update update) {
        UserSettingDto userSettingDto = userSettingClientApi.getByUserId(userDto.getId());
        userSettingDto.setRemindAboutTrial(!userSettingDto.isRemindAboutTrial());
        userSettingClientApi.update(userSettingDto);
        return userSettingService.fillMessageWithAllSettings(userSettingDto, update);
    }

    @Override
    @CacheEvict(value = "users", key = "#userDto.telegramId")
    public List<BotApiMethod> updateShowAllTranslate(UserDto userDto, Update update) {
        UserSettingDto userSettingDto = userSettingClientApi.getByUserId(userDto.getId());
        userSettingDto.setShowAllTranslate(!userSettingDto.isShowAllTranslate());
        userSettingClientApi.update(userSettingDto);
        return userSettingService.fillMessageWithAllSettings(userSettingDto, update);
    }

    @Override
    @CacheEvict(value = "users", key = "#userDto.telegramId")
    public List<BotApiMethod> updateWordCountInTrial(UserDto userDto, WordCountCD wordCountCD, Update update) {
        UserSettingDto userSettingDto = userSettingClientApi.getByUserId(userDto.getId());
        userSettingDto.setWordCountInTrial(wordCountCD.getCount());
        userSettingClientApi.update(userSettingDto);
        return userSettingService.fillMessageWithAllSettings(userSettingDto, update);

    }

    @Override
    @CacheEvict(value = "users", key = "#userDto.telegramId")
    public List<BotApiMethod> updateTranslateCount(UserDto userDto, TranslateCountCD translateCountCD, Update update) {
        UserSettingDto userSettingDto = userSettingClientApi.getByUserId(userDto.getId());
        userSettingDto.setAnswerOptionsCount(translateCountCD.getCount());
        userSettingClientApi.update(userSettingDto);
        return userSettingService.fillMessageWithAllSettings(userSettingDto, update);

    }

}
