package ru.maza.telegram.domain.service.impl;

import com.vdurmont.emoji.EmojiManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.domain.service.UserSettingService;
import ru.maza.telegram.dto.UserSettingDto;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.settings.LearnedWordCountButton;
import ru.maza.telegram.dto.buttons.settings.ScheduleTrialButton;
import ru.maza.telegram.dto.buttons.settings.ShowAllTranslateButton;
import ru.maza.telegram.dto.buttons.settings.TranslateOptionsCountSettingButton;
import ru.maza.telegram.dto.buttons.settings.WordCountSettingButton;
import ru.maza.telegram.utils.EmojiUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserSettingServiceImpl implements UserSettingService {

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    private static final String OK = EmojiManager.getForAlias("white_check_mark").getUnicode();
    private static final String NOT = EmojiManager.getForAlias("x").getUnicode();

    @Override
    public List<BotApiMethod> fillMessageWithAllSettings(UserSettingDto userSettingDto, Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setText(getMessage("settings.my"));
        List<Button> buttons = new java.util.ArrayList<>(List.of(
                new TranslateOptionsCountSettingButton(
                        userSettingDto.getId(),
                        getNextAnswerOptionsCount(userSettingDto.getAnswerOptionsCount()),
                        getMessage(
                                "setting.translate.options.count",
                                EmojiUtils.extractEmojiPercent(userSettingDto.getAnswerOptionsCount())
                        ),
                        1
                ),
                new WordCountSettingButton(
                        userSettingDto.getId(),
                        getNextWordCountInTrial(userSettingDto.getWordCountInTrial()),
                        getMessage(
                                "setting.word.count.trial",
                                EmojiUtils.extractEmojiPercent(userSettingDto.getWordCountInTrial())
                        ),
                        1
                ),
                new LearnedWordCountButton(
                        getMessage(
                                "setting.learned.word.count",
                                EmojiUtils.extractEmojiPercent(userSettingDto.getLearnedWordCount())
                        ),
                        1,
                        getLearnedWordCount(userSettingDto.getLearnedWordCount())

                ),
                new ScheduleTrialButton(
                        getMessage("setting.is.schedule", userSettingDto.isRemindAboutTrial() ? OK : NOT),
                        1
                ),
                new ShowAllTranslateButton(
                        getMessage("setting.is.show.translate", userSettingDto.isShowAllTranslate() ? OK : NOT),
                        1
                )

        ));
        buttons.add(new CancelButton(getMessage("button.cancel"), "/start", 1));
        InlineKeyboardMarkup keyboard = telegramService.getKeyboardMarkup2(buttons);
        editMessage.setReplyMarkup(keyboard);
        return Collections.singletonList(editMessage);
    }

    private Integer getLearnedWordCount(Integer learnedWordCount) {
        if (learnedWordCount == 3) {
            return 4;
        } else if (learnedWordCount == 4) {
            return 5;
        } else if (learnedWordCount == 5) {
            return 6;
        } else if (learnedWordCount == 6) {
            return 7;
        } else { return 3; }
    }

    private Integer getNextAnswerOptionsCount(Integer answerOptionsCount) {
        if (answerOptionsCount == 3) {
            return 4;
        } else if (answerOptionsCount == 4) {
            return 5;
        } else if (answerOptionsCount == 5) {
            return 6;
        } else { return 4; }
    }

    private Integer getNextWordCountInTrial(Integer wordCountInTrial) {
        if (wordCountInTrial == 20) {
            return 30;
        } else if (wordCountInTrial == 30) {
            return 40;
        } else if (wordCountInTrial == 40) {
            return 50;
        } else { return 20; }

    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }


}
