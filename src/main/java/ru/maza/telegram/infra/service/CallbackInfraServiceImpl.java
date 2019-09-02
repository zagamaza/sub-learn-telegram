package ru.maza.telegram.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.domain.service.CallbackService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.ResultRequestDto;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.TrialRequestDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.ChooseTranslateCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.infra.client.TrialWordClientApi;
import ru.maza.telegram.infra.client.TranslatorClientApi;
import ru.maza.telegram.infra.client.TrialClientApi;
import ru.maza.telegram.infra.client.WordClientApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CallbackInfraServiceImpl implements CallbackInfraService {

    private final TrialWordClientApi trialWordClientApi;
    private final TrialClientApi trialClientApi;
    private final CallbackService callbackService;
    private final TelegramService telegramService;
    private final TranslatorClientApi translatorClientApi;
    private final WordClientApi wordClientApi;

    @Override
    public CallbackData getCallbackData(String data) {
        return callbackService.getCallbackData(data);
    }

    @Override
    public List<BotApiMethod> saveResult(ChooseTranslateCD chooseTranslateCD, Update update) {
        trialWordClientApi.save(ResultRequestDto.from(chooseTranslateCD));
        return List.of(callbackService.checkTranslation(chooseTranslateCD, update));
    }

    @Override
    public List<BotApiMethod> getNextWord(Long trialId, Update update) throws InterruptedException {
        Thread.sleep(1000);
        TranslateOptionDto translateOptionDto = trialClientApi.getNextWord(trialId);
        if (translateOptionDto.getTranslatable() == null) {
            return callbackService.finishTrial(translateOptionDto, update);
        }
        return callbackService.fillMessageTranslateOption(translateOptionDto, update);
    }

    @Override
    public List<BotApiMethod> deleteMessage(Update update) {
        return List.of(telegramService.deleteMessage(update));
    }

    @Override
    public List<BotApiMethod> getTranscription(Long wordId, Update update) {
        WordDto word = wordClientApi.getWord(wordId);
        if (word.getTranscription() == null) {
            word = translatorClientApi.translate(word.getWord());
            word.setId(wordId);
            wordClientApi.update(word);
        }
        return callbackService.fillAnswerAlertForWord(word, update);

    }

    @Override
    public List<BotApiMethod> chooseTrial(ChooseTrialCD chooseTrialCD, Update update) {
        TrialDto trialDto = trialClientApi.get(chooseTrialCD.getTrialId());
        if (chooseTrialCD.isStart()) {
            TranslateOptionDto translateOptionDto = trialClientApi.getNextWord(trialDto.getId());
            return callbackService.fillMessageTranslateOption(translateOptionDto, update);
        } else {
            return callbackService.getStatisticByTrial(trialDto, update);
        }
    }

    @Override
    public List<BotApiMethod> startTrial(TrialRequestDto trialRequestDto, Update update) {
        TrialDto trial = trialClientApi.createTrial(trialRequestDto);
        TranslateOptionDto translateOptionDto = trialClientApi.getNextWord(trial.getId());
        return callbackService.fillMessageTranslateOption(translateOptionDto, update);

    }

}
