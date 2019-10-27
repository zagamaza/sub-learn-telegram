package ru.maza.telegram.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.maza.telegram.domain.service.EpisodeService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.buttons.AddFileButton;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.ChooseStartTrialButton;
import ru.maza.telegram.dto.buttons.DeleteCollectionButton;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.utils.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    @Override
    public List<BotApiMethod> getMessageForGetFile(EpisodeDto episodeDto, Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        editMessage.setText(getMessage("episode.get.file"));
        return Collections.singletonList(editMessage);
    }

    @Override
    public List<BotApiMethod> afterSaveSub(EpisodeDto episodeDto, Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(getMessage("want.trial.message"));

        List<Button> buttons = new ArrayList<>();
        buttons.add(ChooseStartTrialButton.from(episodeDto.getId(), null, getMessage("button.start.trial"), 1));
        buttons.add(new CancelButton(
                getMessage("button.cancel"),
                new CancelCD(CancelCD.class.getSimpleName(), "/start"),
                2
        ));

        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return List.of(sendMessage);
    }

    @Override
    public List<BotApiMethod> getMessageChooseSerial(CollectionDto collectionDto, Update update) {
        return null;
    }

    @Override
    public List<BotApiMethod> getMessageChooseFilm(
            CollectionDto collectionDto,
            EpisodeDto episodeDto,
            TrialDto trialDto,
            Update update
    ) {
        EditMessageText editMessage;
        SendMessage sendMessage;
        String text = getMessage(
                "film.name",
                collectionDto.getName(),
                EmojiUtils.extractEmojiPercent(episodeDto.getLearnedPercent())
        );
        List<Button> buttons = new ArrayList<>();
        buttons.add(ChooseStartTrialButton.from(episodeDto.getId(), null, getMessage("button.start.trial"), 1));
        if (trialDto != null) {
            buttons.add(ChooseStartTrialButton.from(episodeDto.getId(), trialDto.getId(), getMessage("button.continue.trial"), 1));
        }
        buttons.add(new DeleteCollectionButton(collectionDto.getId(), getMessage("button.delete.collection"), 2));
        buttons.add(new AddFileButton(episodeDto.getId(), "Add file", 1));
        buttons.add(new CancelButton(
                getMessage("button.cancel.back"),
                new CancelCD(CancelCD.class.getSimpleName(), "/my_collection"),
                2
        ));
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);

        if (update.getCallbackQuery().getMessage() != null) {
            editMessage = telegramService.getEditMessage(update);
            editMessage.setReplyMarkup(keyboardMarkup);
            editMessage.setText(text);
            return Collections.singletonList(editMessage);
        } else {
            sendMessage = telegramService.getSendMessage(update);
            sendMessage.setReplyMarkup(keyboardMarkup);
            sendMessage.setText(text);
            return Collections.singletonList(sendMessage);
        }
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
