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
import ru.maza.telegram.domain.service.CollectionService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;
import ru.maza.telegram.dto.Lang;
import ru.maza.telegram.dto.Page;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.buttons.AddCollectionsButton;
import ru.maza.telegram.dto.buttons.AddPersonalCollectionsButton;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.buttons.CancelButton;
import ru.maza.telegram.dto.buttons.ChooseCollectionButton;
import ru.maza.telegram.dto.buttons.ChooseIsSerialButton;
import ru.maza.telegram.dto.buttons.DeleteCollectionButton;
import ru.maza.telegram.dto.buttons.PageButton;
import ru.maza.telegram.dto.buttons.SearchCollectionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private static final String LEFT = EmojiManager.getForAlias("arrow_left").getUnicode();
    private static final String RIGHT = EmojiManager.getForAlias("arrow_right").getUnicode();

    private final TelegramService telegramService;
    private final MessageSource messageSource;

    public CollectionRequest fillCollectionRequest(Update update, UserDto userDto) {
        return CollectionRequest.builder()
                                .created(LocalDateTime.now())
                                .name(telegramService.getTextMessage(update))
                                .isSerial(false)
                                .isShared(false)
                                .rating(0)
                                .lang(Lang.EN_RU)
                                .userId(userDto.getId())
                                .build();
    }

    @Override
    public List<BotApiMethod> fillMessageHowYouAddCollection(Update update) {
        EditMessageText editMessageText = telegramService.getEditMessage(update);
        editMessageText.setText(getMessage("choose.add.collection.option"));
        List<Button> buttons = new ArrayList<>();
        buttons.add(new SearchCollectionButton(getMessage("button.search.collection"), 2));
        buttons.add(new AddPersonalCollectionsButton(null, null, getMessage("button.add.personal.collection"), 1));
        buttons.add(new CancelButton(getMessage("button.cancel.back"), "/my_collection", 2));
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(buttons);
        editMessageText.setReplyMarkup(keyboardMarkup);
        return Collections.singletonList(editMessageText);
    }

    @Override
    public List<BotApiMethod> getWindowAllCollection(
            Page page, List<CollectionCondensedDto> collections,
            UserDto userDto,
            Update update
    ) {
        EditMessageText editMessage = telegramService.getEditMessage(update);
        InlineKeyboardMarkup keyboard = null;
        List<Button> collect = new ArrayList<>();
        if (isEmpty(collections)) {
            editMessage.setText(getMessage("collection.empty.message"));
        } else {
            editMessage.setText(getMessage("collections.my"));
            collect = collections.stream()
                                 .map(collectionCondensedDto ->
                                              new ChooseCollectionButton(
                                                      collectionCondensedDto.getId(),
                                                      collectionCondensedDto.getIsSerial(),
                                                      collectionCondensedDto.getName(),
                                                      1
                                              ))
                                 .collect(Collectors.toList());
        }
        if (page.getPage() != 0) {collect.add(new PageButton("collection", page.getPage() - 1, true, LEFT, 2));}
        collect.add(new CancelButton(getMessage("button.cancel"), "/start", 2));
        if ((page.getPage() + 1) * 10 < page.getCount()) {
            collect.add(new PageButton("collection", page.getPage() + 1, false, RIGHT, 1));
        }
        collect.add(new AddCollectionsButton(getMessage("button.add.file"), 2));
        keyboard = telegramService.getKeyboardMarkup2(collect);
        editMessage.setReplyMarkup(keyboard);
        return Collections.singletonList(editMessage);
    }

    public List<BotApiMethod> fillMessageCollectionCreated(CollectionDto collectionDto, Update update) {
        SendMessage sendMessage = telegramService.getSendMessage(update);
        sendMessage.setText(getMessage("collection.created", collectionDto.getName()));

        SendMessage sendMessage1 = telegramService.getSendMessage(update);
        sendMessage1.setText(getMessage("collection.choose.isSerial"));
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(List.of(
                ChooseIsSerialButton.from(collectionDto.getId(), false),
                ChooseIsSerialButton.from(collectionDto.getId(), true)
        ));
        sendMessage1.setReplyMarkup(keyboardMarkup);
        return List.of(sendMessage, sendMessage1);
    }

    @Override
    public List<BotApiMethod> getAlertCopiedCollection(Update update, CollectionDto collection) {
        String text = getMessage("exception.message");
        return Collections.singletonList(telegramService.addAnswerCallbackQuery(update.getCallbackQuery(), true, text));
    }

    @Override
    public List<BotApiMethod> getMessageDeleteCollection(CollectionDto collectionDto, Update update) {
        String text = getMessage("collection.not.found", collectionDto.getName());
        InlineKeyboardMarkup keyboardMarkup = telegramService.getKeyboardMarkup2(List.of(new DeleteCollectionButton(
                collectionDto.getId(),
                getMessage("button.delete.collection"),
                1
        )));
        return telegramService.getTelegramMessage(update, keyboardMarkup, text);
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}
