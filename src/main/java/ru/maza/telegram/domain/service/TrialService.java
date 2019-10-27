package ru.maza.telegram.domain.service;

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

    List<BotApiMethod> fillMessageTranslateOption(TranslateOptionDto translateOptionDto, Update update);

    List<BotApiMethod> finishTrial(TrialDto trialDto, Update update);

    List<BotApiMethod> fillMessageWithAllTrial(Page page, List<TrialCondensedDto> lastConsedTrial, Update update);

    List<BotApiMethod> fillAlertStatisticByTrial(TrialDto trialDto, Update update);

    BotApiMethod getAlertWithAllTranslate(WordDto word, Update update);

    List<BotApiMethod> fillMessageRepeatTrial(Long id, UserDto userDto, Update update);

}
