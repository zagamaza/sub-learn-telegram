package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface NotificationInfraService {

    List<BotApiMethod> getTextNotifications();

    void updateCallbackNotifications();

    AnswerCallbackQuery getRandomAnswerCallback(Update update);

    AnswerCallbackQuery getAnswerCallback();

}
