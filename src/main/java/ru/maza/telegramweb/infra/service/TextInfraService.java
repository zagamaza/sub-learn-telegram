package ru.maza.telegramweb.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegramweb.dto.UserDto;

import java.util.List;

public interface TextInfraService {
    List<BotApiMethod> getCollectionsByUserId(Update update);

    List<BotApiMethod> getTrialsByUserId(Update update);

    UserDto saveUser(Update update);

    List<BotApiMethod> translateWord(String answer, Update update);
}
