package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotService {

    List<BotApiMethod> getMessageForStart(Update update);

    List<BotApiMethod> fillMessageHowYouAddCollection(Update update);

    List<BotApiMethod> getEditMessageForStart(Update update);

    List<BotApiMethod> getExceptionMessage(Update update);



}