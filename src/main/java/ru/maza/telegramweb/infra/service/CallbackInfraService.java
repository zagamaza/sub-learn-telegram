package ru.maza.telegramweb.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegramweb.dto.TrialRequestDto;
import ru.maza.telegramweb.dto.callbackData.CallbackData;
import ru.maza.telegramweb.dto.callbackData.ChooseTranslateCD;
import ru.maza.telegramweb.dto.callbackData.ChooseTrialCD;

import java.util.List;

public interface CallbackInfraService {

    CallbackData getCallbackData(String data);

    List<BotApiMethod> saveResult(ChooseTranslateCD chooseTranslateCD, Update update);

    List<BotApiMethod> getNextWord(Long trialId, Update update) throws InterruptedException;

    List<BotApiMethod> deleteMessage(Update update);

    List<BotApiMethod> getTranscription(Long wordId, Update update);

    List<BotApiMethod> chooseTrial(ChooseTrialCD chooseTrialCD, Update update);

    List<BotApiMethod> startTrial(TrialRequestDto trialRequestDto, Update update);

}
