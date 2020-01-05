package ru.maza.telegram.domain.service.impl;

import com.vdurmont.emoji.EmojiManager;
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
import ru.maza.telegram.dto.Constant;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.buttons.AddFileButton;
import ru.maza.telegram.dto.buttons.AddPersonalCollectionsButton;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.ChooseSeasonButton;
import ru.maza.telegram.dto.buttons.ChooseSeriesButton;
import ru.maza.telegram.dto.buttons.ChooseStartTrialButton;
import ru.maza.telegram.dto.buttons.DeleteCollectionButton;
import ru.maza.telegram.dto.buttons.DeleteEpisodeButton;
import ru.maza.telegram.dto.buttons.PageButton;
import ru.maza.telegram.dto.buttons.PageSeasonButton;
import ru.maza.telegram.dto.buttons.PageSerialButton;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.utils.EmojiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.maza.telegram.utils.EmojiUtils.ALLOW_LEFT;
import static ru.maza.telegram.utils.EmojiUtils.ALLOW_RIGHT;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private static final String LEFT = EmojiManager.getForAlias("arrow_left").getUnicode();
    private static final String RIGHT = EmojiManager.getForAlias("arrow_right").getUnicode();

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    @Override
    public List<BotApiMethod> getMessageForGetFile(Update update) {
        String text = getMessage("episode.get.file");
        return telegramService.getTelegramMessage(update, null, text);
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
    public List<BotApiMethod> getMessageChooseSerial(
            Page page, List<Integer> seasons,
            CollectionDto collectionDto,
            Update update
    ) {
        List<Button> buttons = seasons.stream()
                                      .map(season -> new ChooseSeasonButton(
                                                   getMessage("button.season", EmojiUtils.extractEmojiPercent(season)),
                                                   collectionDto.getId(),
                                                   season,
                                                   1
                                           )
                                      )
                                      .collect(Collectors.toList());
        buttons.add(new DeleteCollectionButton(collectionDto.getId(), getMessage("button.delete.collection"), 1));

        if (page.getPage() != 0) {
            buttons.add(new PageSeasonButton(collectionDto.getId(),page.getPage() - 1, true, ALLOW_LEFT, 2));
        }
        buttons.add(new CancelButton(getMessage("button.cancel.back"), Constant.MY_COLLECTION, 2));

        if ((page.getPage() + 1) * 10 < page.getCount()) {
            buttons.add(new PageSeasonButton(collectionDto.getId(),page.getPage() + 1, false, ALLOW_RIGHT, 1));
        }

        if (!collectionDto.isShared()) {
            buttons.add(new AddPersonalCollectionsButton(
                    collectionDto.getId(),
                    null,
                    getMessage("button.add.file"),
                    2
            ));
        }
        String text = getMessage("serial.choose.name", collectionDto.getName());
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        return telegramService.getTelegramMessage(update, keyboardMarkup, text);
    }

    @Override
    public List<BotApiMethod> getMessageChooseFilm(
            CollectionDto collectionDto,
            EpisodeDto episodeDto,
            TrialDto trialDto,
            Update update
    ) {
        String text = getMessage(
                "film.name",
                collectionDto.getName(),
                EmojiUtils.extractEmojiPercent(episodeDto.getLearnedPercent())
        );
        List<Button> buttons = new ArrayList<>();
        buttons.add(ChooseStartTrialButton.from(episodeDto.getId(), null, getMessage("button.start.trial"), 1));
        if (trialDto != null) {
            buttons.add(ChooseStartTrialButton.from(
                    episodeDto.getId(),
                    trialDto.getId(),
                    getMessage("button.continue.trial"),
                    1
            ));
        }
        buttons.add(new DeleteCollectionButton(collectionDto.getId(), getMessage("button.delete.collection"), 2));
        if (!episodeDto.getCollectionDto().isShared()) {
            buttons.add(new AddFileButton(episodeDto.getId(), "Добавить файл", 1));
        }
        buttons.add(new CancelButton(
                getMessage("button.cancel.back"),
                new CancelCD(CancelCD.class.getSimpleName(), Constant.MY_COLLECTION),
                2
        ));
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        return telegramService.getTelegramMessage(update, keyboardMarkup, text);
    }

    @Override
    public List<BotApiMethod> getMessageChooseSeason(Page page, List<EpisodeDto> episodes, Update update) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        InlineKeyboardMarkup keyboard;
        List<Button> collect = new ArrayList<>();
        EpisodeDto episode = episodes.get(0);
        if (isEmpty(episodes)) {
            editMessage.setText(getMessage("collection.empty.message"));
        } else {
            editMessage.setText(getMessage(
                    "season.choose",
                    episode.getCollectionDto().getName(),
                    EmojiUtils.extractEmojiPercent(episode.getSeason())
            ));
            collect = episodes.stream()
                              .map(episodeDto ->
                                           new ChooseSeriesButton(
                                                   episodeDto.getCollectionDto().getId(),
                                                   episodeDto.getId(),
                                                   getMessage(
                                                           "button.series",
                                                           EmojiUtils.extractEmojiPercent(episodeDto.getEpisode())
                                                   ),
                                                   1
                                           ))
                              .collect(Collectors.toList());
        }
        if (page.getPage() != 0) {
            collect.add(new PageSerialButton(
                    episode.getSeason(),
                    episode.getCollectionDto().getId(),
                    page.getPage() - 1,
                    true,
                    LEFT,
                    2
            ));
        }
        collect.add(new CancelButton(getMessage("button.cancel.back"), Constant.MY_COLLECTION, 2));
        if ((page.getPage() + 1) * 10 < page.getCount()) {
            collect.add(new PageSerialButton(
                    episode.getSeason(),
                    episode.getCollectionDto().getId(),
                    page.getPage() + 1,
                    false,
                    RIGHT,
                    1
            ));
        }
        if (!episode.getCollectionDto().isShared()) {
            collect.add(new AddPersonalCollectionsButton(
                    null,
                    episode.getId(),
                    getMessage("button.add.file"),
                    2
            ));
        }
        keyboard = telegramService.getKeyboardMarkup2(collect);
        editMessage.setReplyMarkup(keyboard);
        return Collections.singletonList(editMessage);
    }

    @Override
    public List<BotApiMethod> getMessageChooseSeries(EpisodeDto episodeDto, TrialDto trialDto, Update update) {
        String text = getMessage(
                "series.choose.name",
                episodeDto.getCollectionDto().getName(),
                EmojiUtils.extractEmojiPercent(episodeDto.getSeason()),
                EmojiUtils.extractEmojiPercent(episodeDto.getEpisode()),
                EmojiUtils.extractEmojiPercent(episodeDto.getLearnedPercent())
        );
        List<Button> buttons = new ArrayList<>();
        buttons.add(ChooseStartTrialButton.from(episodeDto.getId(), null, getMessage("button.start.trial"), 1));
        if (trialDto != null) {
            buttons.add(ChooseStartTrialButton.from(
                    episodeDto.getId(),
                    trialDto.getId(),
                    getMessage("button.continue.trial"),
                    1
            ));
        }
        buttons.add(new CancelButton(
                getMessage("button.cancel.back"),
                new CancelCD(CancelCD.class.getSimpleName(), Constant.MY_COLLECTION),
                2
        ));
        if (!episodeDto.getCollectionDto().isShared()) {
            buttons.add(new DeleteEpisodeButton(episodeDto.getId(), getMessage("button.delete.collection"), 2));
            buttons.add(new AddFileButton(episodeDto.getId(), "Добавить файл", 1));
        }
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        return telegramService.getTelegramMessage(update, keyboardMarkup, text);
    }

    @Override
    public List<BotApiMethod> getMessageForSetSeason(Update update) {
        EditMessageText editMessageText = telegramService.getEditMessage(update);
        editMessageText.setText(getMessage("episode.get.season"));
        return Collections.singletonList(editMessageText);
    }

    @Override
    public List<BotApiMethod> getMessageForSetSerial(EpisodeDto episodeDto, Update update) {
        String message = getMessage("episode.get.series");
        return telegramService.getTelegramMessage(update, null, message);
    }

    @Override
    public List<BotApiMethod> getAlertSeasonAlreadyExists(EpisodeDto episodeDto, Update update) {
        String alertText = getMessage(
                "serial.season.already.exists",
                EmojiUtils.extractEmojiPercent(episodeDto.getSeason()),
                episodeDto.getCollectionDto().getName()
        );
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(alertText);
        return Collections.singletonList(sendMessage);
    }

    @Override
    public List<BotApiMethod> getAlertSeriesAlreadyExists(EpisodeDto episodeDto, Update update) {
        String alertText = getMessage(
                "serial.series.already.exists",
                EmojiUtils.extractEmojiPercent(episodeDto.getEpisode()),
                EmojiUtils.extractEmojiPercent(episodeDto.getSeason()),
                episodeDto.getCollectionDto().getName()
        );
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(alertText);
        return Collections.singletonList(sendMessage);
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
