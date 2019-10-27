package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.callbackData.CallbackData;

import java.util.List;

public interface CallbackInfraService {

    CallbackData getCallbackData(String data);

    List<BotApiMethod> deleteMessage(Update update);

    List<BotApiMethod> getTranscription(Long wordId, Update update);

    List<BotApiMethod> searchCollection(Update update);

}
