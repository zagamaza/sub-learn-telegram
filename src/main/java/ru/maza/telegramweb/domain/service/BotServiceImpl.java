package ru.maza.telegramweb.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegramweb.dto.CollectionCondensedDto;
import ru.maza.telegramweb.dto.TrialCondensedDto;
import ru.maza.telegramweb.dto.WordDto;
import ru.maza.telegramweb.dto.buttons.Button;
import ru.maza.telegramweb.dto.buttons.CancelButton;
import ru.maza.telegramweb.dto.buttons.ChooseCollectionButton;
import ru.maza.telegramweb.dto.buttons.ChooseTrialButton;
import ru.maza.telegramweb.dto.buttons.TranscriptionButton;
import ru.maza.telegramweb.dto.callbackData.CancelCD;
import ru.maza.telegramweb.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegramweb.dto.callbackData.ChooseTrialCD;
import ru.maza.telegramweb.dto.callbackData.TranscriptionCD;

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

    private String getMessage(String key, Object... args) {
        return this.messageSource.getMessage(key, args, Locale.getDefault());
    }

}

