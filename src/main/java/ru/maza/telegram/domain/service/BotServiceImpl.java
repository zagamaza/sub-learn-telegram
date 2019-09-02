package ru.maza.telegram.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.TrialCondensedDto;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.buttons.*;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.TranscriptionCD;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotServiceImpl {

    private final TelegramService telegramService;
    private final MessageSource messageSource;


    public List<BotApiMethod> fillMessageLastCondensedTrial(List<TrialCondensedDto> lastConsedTrial, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramService.getChatId(update));
        List<Button> collect = lastConsedTrial.stream()
                .map(trialCondensedDto ->
                        new ChooseTrialButton(
                                trialCondensedDto.getCollectionName() + " " + trialCondensedDto
                                        .getPercent() + "%",
                                new ChooseTrialCD(
                                        ChooseTrialCD.class.getSimpleName(),
                                        trialCondensedDto.getId(),
                                        false
                                ),
                                1,
                                trialCondensedDto.getId(),
                                false
                        ))
                .collect(Collectors.toList());
        fillOtherButton(collect);

        sendMessage.setReplyMarkup(telegramService.getKeyboardMarkup2(collect));
        sendMessage.setText(getMessage("trial.last.message"));
        return Collections.singletonList(sendMessage);

    }

    public List<BotApiMethod> fillTranslateOptionDto(WordDto translate, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramService.getChatId(update));
        String ss = getMessage(
                "word.translate.message",
                translate.getWord(),
                translate.getTranscription(),
                String.join(",", translate.getTranslation())
        );
        sendMessage.setText(ss);
        return Collections.singletonList(sendMessage);
    }

    public List<BotApiMethod> fillMessageLastCollection(List<CollectionCondensedDto> collections, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(telegramService.getChatId(update));
        List<Button> collect = collections.stream()
                .map(collectionCondensedDto ->
                        new ChooseCollectionButton(
                                collectionCondensedDto.getId(),
                                collectionCondensedDto.getName(),
                                new ChooseCollectionCD(
                                        ChooseCollectionCD.class.getSimpleName(),
                                        collectionCondensedDto.getId()
                                ),
                                1
                        ))
                .collect(Collectors.toList());
        fillOtherButton(collect);

        sendMessage.setReplyMarkup(telegramService.getKeyboardMarkup2(collect));
        sendMessage.setText(getMessage("collection.last.message"));
        return Collections.singletonList(sendMessage);

    }

    private void fillOtherButton(List<Button> buttons) {
        buttons.add(new TranscriptionButton(" ", new TranscriptionCD(TranscriptionCD.class.getSimpleName(), 1L), 2));
        buttons.add(new CancelButton(getMessage("cancel"), new CancelCD(CancelCD.class.getSimpleName(), true), 1));
    }

    public List<BotApiMethod> fillMessageSetMeNameForCollection(Update update) {
        SendMessage sendMessage = getNewMessage(update);
        telegramService.setForceReplyKeyboard(sendMessage);
        sendMessage.setText(getMessage("collection.send.name"));
        return List.of(sendMessage);
    }

    private SendMessage getNewMessage(Update update) {
        return new SendMessage().setChatId(telegramService.getChatId(update));
    }

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

    public List<BotApiMethod> fillMessageCollectionCreated(CollectionDto collectionDto, Update update) {
        SendMessage sendMessage = getNewMessage(update);
        sendMessage.setText(getMessage("collection.created", collectionDto.getName()));
        return Collections.singletonList(sendMessage);
    }

}

