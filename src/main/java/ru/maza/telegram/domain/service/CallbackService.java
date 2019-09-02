package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.ChooseTranslateCD;

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
