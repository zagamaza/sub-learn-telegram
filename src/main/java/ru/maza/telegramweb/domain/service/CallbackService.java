package ru.maza.telegramweb.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegramweb.dto.CollectionDto;
import ru.maza.telegramweb.dto.TranslateOptionDto;
import ru.maza.telegramweb.dto.TrialDto;
import ru.maza.telegramweb.dto.WordDto;
import ru.maza.telegramweb.dto.callbackData.CallbackData;
import ru.maza.telegramweb.dto.callbackData.ChooseTranslateCD;

import java.util.List;

public interface CallbackService {
    BotApiMethod checkTranslation(ChooseTranslateCD chooseTranslateCD, Update update);

    List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update);

    CallbackData getCallbackData(String data);

    List<BotApiMethod> fillAnswerAlertForWord(WordDto wordDto, Update update);

    List<BotApiMethod> getStatisticByTrial(TrialDto trialDto, Update update);

    List<BotApiMethod> afterSaveCollection(CollectionDto collectionDto, Update update);

    List<BotApiMethod> finishTrial(TranslateOptionDto translateOptionDto, Update update);
}
