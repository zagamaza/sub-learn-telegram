package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialCondensedDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.callbackData.CTlteCD;

import java.util.List;

public interface TrialService {

    BotApiMethod checkTranslation(CTlteCD chooseTranslateCD, Update update);

    BotApiMethod setRightTranslation(Long wordId, Update update);

    List<BotApiMethod> fillMessageTranslateOption(
            List<Boolean> trialWordStatus,
            TranslateOptionDto translateOptionDto,
            Update update
    );

    BotApiMethod finishTrial(List<Boolean> trialWordStatus, AnswerCallbackQuery callbackQuery, TrialDto trialDto, Update update);

    List<BotApiMethod> fillMessageWithAllTrial(Page page, List<TrialCondensedDto> lastConsedTrial, Update update);

    List<BotApiMethod> fillAlertStatisticByTrial(
            List<Boolean> trialWordStatus,
            TrialDto trialDto,
            Update update
    );

    BotApiMethod getAlertWithAllTranslate(WordDto word, Update update);

    List<BotApiMethod> fillMessageRepeatTrial(Integer learnedPercent, Long id, UserDto userDto, Update update);

}
