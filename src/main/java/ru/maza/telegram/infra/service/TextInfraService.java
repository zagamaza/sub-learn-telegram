package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;

import java.util.List;

public interface TextInfraService {

    UserDto saveUser(Update update, Integer userId);

    List<BotApiMethod> translateWord(String answer, Update update);

}
