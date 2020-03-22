package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.TranslateCountCD;
import ru.maza.telegram.dto.callbackData.WordCountCD;

import java.util.List;

public interface UserSettingInfraService {

    List<BotApiMethod> getMySettings(UserDto userDto, Update update);

    List<BotApiMethod> updateSchedule(UserDto userDto, Update update);

    List<BotApiMethod> updateShowAllTranslate(UserDto userDto, Update update);

    List<BotApiMethod> updateWordCountInTrial(UserDto userDto, WordCountCD wordCountCD, Update update);

    List<BotApiMethod> updateTranslateCount(UserDto userDto, TranslateCountCD translateCountCD, Update update);

    List<BotApiMethod> updateLearnedWordCount(Integer count, UserDto userDto, Update update);

    BotApiMethod resetProgress(UserDto userDto, Update update);

}
