package ru.maza.telegram.infra.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.TrialClientApi;
import ru.maza.telegram.client.TrialWordClientApi;
import ru.maza.telegram.client.WordClientApi;
import ru.maza.telegram.client.impl.EpisodeClient;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.domain.service.TrialService;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialCondensedDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.TrialRequest;
import ru.maza.telegram.dto.TrialWordDto;
import ru.maza.telegram.dto.TrialWordRequest;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.CCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.infra.mq.UserMQ;
import ru.maza.telegram.infra.service.NotificationInfraService;
import ru.maza.telegram.infra.service.TrialInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrialInfraServiceImpl implements TrialInfraService {

    private final NotificationInfraService notificationInfraService;
    private final TrialClientApi trialClientApi;
    private final TrialWordClientApi trialWordClientApi;
    private final WordClientApi wordClientApi;
    private final EpisodeClient episodeClient;
    private final TrialService trialService;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public List<BotApiMethod> startTrial(UserDto userDto, Update update, Long episodeId) {
        TrialRequest trialRequestDto = TrialRequest.get(episodeId, userDto.getId());
        TrialDto trial = trialClientApi.saveTrialAnd20TrialWord(trialRequestDto);
        TranslateOptionDto translateOptionDto = trialClientApi.getTranslateOptionDto(trial.getId());
        List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trial.getId());
        return trialService.fillMessageTranslateOption(trialWordStatus, translateOptionDto, update);
    }

    @Override
    public List<BotApiMethod> saveAndCheckResult(CCD chooseTranslateCD, UserDto userDto, Update update) {
        TrialWordRequest trialWordRequest = TrialWordRequest.from(chooseTranslateCD);
        sendMQ(userDto, chooseTranslateCD);
        trialWordClientApi.updateTrialWordAndSaveUserWord(trialWordRequest);
        return List.of(trialService.checkTranslation(chooseTranslateCD, update));
    }

    private void sendMQ(UserDto userDto, CCD chooseTranslateCD) {
        if (chooseTranslateCD.getR().equals(chooseTranslateCD.getW())) {
            String userName = userDto.getUserName().replace("_null", "");
            UserMQ userMQ = new UserMQ(userDto.getId(), userDto.getTelegramId(), userName);
            rabbitTemplate.convertAndSend(userMQ);
        }
    }

    @Override
    public List<BotApiMethod> getNextWord(Long trialId, Update update) {
        TranslateOptionDto translateOptionDto;
        try {
            translateOptionDto = trialClientApi.getTranslateOptionDto(trialId);
        } catch (FeignException e) {
            TrialDto trialDto = trialClientApi.get(trialId);
            List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trialId);
            AnswerCallbackQuery advertisingAnswerCallback = notificationInfraService.getAnswerCallback();
            return List.of(trialService.finishTrial(trialWordStatus, advertisingAnswerCallback, trialDto, update));
        }
        List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trialId);
        return trialService.fillMessageTranslateOption(trialWordStatus, translateOptionDto, update);
    }

    @Override
    public List<BotApiMethod> getAllTrials(UserDto userDto, Update update) {
        RestPageImpl<TrialCondensedDto> lastConsedTrial = trialClientApi.getLastConsedTrial(
                userDto.getId(),
                PageRequest.of(0, 10, Sort.by("id").descending())
        );
        Integer collectionCount = (int)lastConsedTrial.getTotalElements();
        Page page = new Page(collectionCount, 0);
        return trialService.fillMessageWithAllTrial(page, lastConsedTrial.getContent(), update);
    }

    @Override
    public List<BotApiMethod> chooseTrial(ChooseTrialCD chooseTrialCD, Update update) {
        TrialDto trialDto = trialClientApi.get(chooseTrialCD.getTrialId());
        List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(chooseTrialCD.getTrialId());
        return trialService.fillAlertStatisticByTrial(trialWordStatus, trialDto, update);
    }

    @Override
    public BotApiMethod getAlertWithAllTranslates(Long wordId, Update update) {
        return trialService.getAlertWithAllTranslate(wordClientApi.getWord(wordId), update);
    }

    @Override
    public List<BotApiMethod> getTrialsByPage(UserDto userDto, PageCD pageCD, Update update) {
        RestPageImpl<TrialCondensedDto> lastConsedTrial = trialClientApi.getLastConsedTrial(
                userDto.getId(),
                PageRequest.of(pageCD.getPage(), 10, Sort.by("id"))
        );
        Integer collectionCount = (int)lastConsedTrial.getTotalElements();
        Page page = new Page(collectionCount, pageCD.getPage());
        return trialService.fillMessageWithAllTrial(page, lastConsedTrial.getContent(), update);
    }

    @Override
    public List<BotApiMethod> repeatTrial(Long tlId, UserDto userDto, Update update) {
        TrialDto trialDto = trialClientApi.get(tlId);
        Integer learnedPercent = episodeClient.getLearnedPercent(trialDto.getEpisodeDto().getId(), userDto.getId());
        return trialService.fillMessageRepeatTrial(learnedPercent, trialDto.getEpisodeDto().getId(), userDto, update);
    }

    @Override
    public List<BotApiMethod> saveLearnedTrialWordAndGetNextWord(Long trialWordId, Boolean isRight, Update update) {
        TrialWordDto trialWordDto = trialWordClientApi.updateTrialWordAndSaveLearnedUserWord(trialWordId, isRight);
        if (trialWordDto.isLastWord()) {
            TrialDto trialDto = trialClientApi.get(trialWordDto.getTrialDto().getId());
            List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trialDto.getId());
            AnswerCallbackQuery answerCallback = notificationInfraService.getAnswerCallback();
            return List.of(trialService.finishTrial(trialWordStatus, answerCallback, trialDto, update));
        }
        return getNextWord(trialWordDto.getTrialDto().getId(), update);
    }

    @Override
    public BotApiMethod setRightWord(Long wdId, Update update) {
        return trialService.setRightTranslation(wdId, update);
    }

}

