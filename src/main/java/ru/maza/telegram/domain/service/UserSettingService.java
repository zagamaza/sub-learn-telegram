package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserSettingDto;

import java.util.List;

public interface UserSettingService {

    List<BotApiMethod> fillMessageWithAllSettings(UserSettingDto userSettingDto, Update update);

}
