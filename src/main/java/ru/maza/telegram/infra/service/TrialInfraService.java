package ru.maza.telegram.infra.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.CTlteCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.PageCD;

import java.util.List;

public interface TrialInfraService {

    List<BotApiMethod> startTrial(UserDto userDto, Update update, Long episodeId);

    List<BotApiMethod> saveAndCheckResult(
            CTlteCD chooseTranslateCD,
            UserDto userDto,
            Update update
    );

    List<BotApiMethod> getNextWord(Long trialId, Update update);

    List<BotApiMethod> getAllTrials(UserDto userDto, Update update);

    List<BotApiMethod> chooseTrial(ChooseTrialCD chooseTrialCD, Update update);

    BotApiMethod getAlertWithAllTranslates(Long wordId, Update update);

    List<BotApiMethod> getTrialsByPage(UserDto userDto, PageCD pageCD, Update update);

    List<BotApiMethod> repeatTrial(Long tlId, UserDto userDto, Update update);

    List<BotApiMethod> saveLearnedTrialWordAndGetNextWord(Long trialWordId, Boolean isRight, Update update);

    BotApiMethod setRightWord(Long wdId, Update update);

}
