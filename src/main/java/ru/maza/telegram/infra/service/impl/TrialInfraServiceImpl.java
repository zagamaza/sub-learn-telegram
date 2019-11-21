package ru.maza.telegram.infra.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
import ru.maza.telegram.dto.callbackData.CTlteCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.infra.service.TrialInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrialInfraServiceImpl implements TrialInfraService {

    private final TrialClientApi trialClientApi;
    private final TrialWordClientApi trialWordClientApi;
    private final WordClientApi wordClientApi;
    private final EpisodeClient episodeClient;
    private final TrialService trialService;

    @Override
    public List<BotApiMethod> startTrial(UserDto userDto, Update update, Long episodeId) {
        TrialRequest trialRequestDto = TrialRequest.get(episodeId, userDto.getId());
        TrialDto trial = trialClientApi.saveTrialAnd20TrialWord(trialRequestDto);
        TranslateOptionDto translateOptionDto = trialClientApi.getTranslateOptionDto(trial.getId());
        List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trial.getId());
        return trialService.fillMessageTranslateOption(trialWordStatus, translateOptionDto, update);
    }

    @Override
    public List<BotApiMethod> saveAndCheckResult(CTlteCD chooseTranslateCD, Update update) {
        trialWordClientApi.updateTrialWordAndSaveUserWord(TrialWordRequest.from(chooseTranslateCD));
        return List.of(trialService.checkTranslation(chooseTranslateCD, update));
    }

    @Override
    public List<BotApiMethod> getNextWord(Long trialId, Update update) {
        TranslateOptionDto translateOptionDto;
        try {
            translateOptionDto = trialClientApi.getTranslateOptionDto(trialId);
        } catch (FeignException e) {
            TrialDto trialDto = trialClientApi.get(trialId);
            List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trialId);
            return trialService.finishTrial(trialWordStatus, trialDto, update);
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
    public List<BotApiMethod> saveLearnedTrialWordAndGetNextWord(Long trialWordId, Update update) {
        TrialWordDto trialWordDto = trialWordClientApi.updateTrialWordAndSaveLearnedUserWord(trialWordId);
        if (trialWordDto.isLastWord()) {
            TrialDto trialDto = trialClientApi.get(trialWordDto.getTrialDto().getId());
            List<Boolean> trialWordStatus = trialClientApi.getTrialWordStatusByTrialId(trialDto.getId());
            return trialService.finishTrial(trialWordStatus, trialDto, update);
        }
        return getNextWord(trialWordDto.getTrialDto().getId(), update);
    }

}

