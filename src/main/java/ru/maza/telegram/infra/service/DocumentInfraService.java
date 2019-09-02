package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;

public interface DocumentInfraService {

    List<BotApiMethod> addSubCollection(File file, Update update);

}
