package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface BotInfraService {

    List<BotApiMethod> getStartWindow(Update update, Boolean isEdit);

}
