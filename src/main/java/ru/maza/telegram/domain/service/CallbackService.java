package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.callbackData.CallbackData;

import java.util.List;

public interface CallbackService {

    List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update);

    CallbackData getCallbackData(String data);

    List<BotApiMethod> fillAnswerAlertForWord(WordDto wordDto, Update update);

    List<BotApiMethod> getStatisticByTrial(TrialDto trialDto, Update update);

}
