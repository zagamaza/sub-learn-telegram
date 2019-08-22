package ru.maza.telegramweb.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.maza.telegramweb.domain.service.TelegramService;
import ru.maza.telegramweb.dto.TrialRequestDto;
import ru.maza.telegramweb.dto.callbackData.CallbackData;
import ru.maza.telegramweb.dto.callbackData.CancelCD;
import ru.maza.telegramweb.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegramweb.dto.callbackData.ChooseTranslateCD;
import ru.maza.telegramweb.dto.callbackData.ChooseTrialCD;
import ru.maza.telegramweb.dto.callbackData.TranscriptionCD;
import ru.maza.telegramweb.infra.service.CallbackInfraService;
import ru.maza.telegramweb.infra.service.DocumentInfraService;
import ru.maza.telegramweb.infra.service.TextInfraService;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BotController extends TelegramLongPollingBot {

    private final TelegramService telegramService;
    private final DocumentInfraService documentInfraService;
    private final TextInfraService textInfraService;
    private final CallbackInfraService callbackInfraService;

    @Value("${tg.token}")
    private String token;

    @Value("${tg.username}")
    private String username;


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String answer = telegramService.getTextMessage(update).toLowerCase();

            textInfraService.saveUser(update);

            switch (answer) {
                case ("/subs"):
                    send(textInfraService.getCollectionsByUserId(update));
                    break;
                case ("/trials"):
                    send(textInfraService.getTrialsByUserId(update));
                    break;
            }
            if (answer.matches("^[a-zA-Z]+$")) {
                send(textInfraService.translateWord(answer, update));
            }

        }
        if (update.hasCallbackQuery()) {

            CallbackData callbackData = callbackInfraService.getCallbackData(update.getCallbackQuery().getData());

            if (callbackData instanceof ChooseCollectionCD) {
                ChooseCollectionCD chooseCollection = (ChooseCollectionCD)callbackData;
                send(callbackInfraService.startTrial(TrialRequestDto.from(
                        chooseCollection,
                        telegramService.getUserId(update)
                ), update));

            } else if (callbackData instanceof ChooseTranslateCD) {
                ChooseTranslateCD chooseTranslateCD = (ChooseTranslateCD)callbackData;
                send(callbackInfraService.saveResult(chooseTranslateCD, update));
                try {
                    send(callbackInfraService.getNextWord(chooseTranslateCD.getTId(), update));
                } catch (InterruptedException e) {}
            } else if (callbackData instanceof CancelCD) {
                send(callbackInfraService.deleteMessage(update));
            } else if (callbackData instanceof TranscriptionCD) {
                TranscriptionCD transcriptionCD = (TranscriptionCD)callbackData;
                send(callbackInfraService.getTranscription(transcriptionCD.getWordId(), update));
            } else if (callbackData instanceof ChooseTrialCD) {
                ChooseTrialCD chooseTrialCD = (ChooseTrialCD)callbackData;
                send(callbackInfraService.chooseTrial(chooseTrialCD, update));
            }


            /**
             * Работа с Emoji для перелистывания между страницами
             */
//                else if (EmojiManager.isEmoji(data.replaceAll("[0-9]", ""))) {
//                    Emoji emoji = EmojiManager.getByUnicode(data.replaceAll("[0-9]", ""));
//                }
        }

        if (update.hasMessage() && update.getMessage().hasDocument()) {
            GetFile getFile = new GetFile();
            getFile.setFileId(update.getMessage().getDocument().getFileId());
            java.io.File file = new java.io.File("src/main/resources/file.txt");
            try {
                File execute = execute(getFile);
                send(telegramService.getSendChatAction(telegramService.getMessage(update), ActionType.UPLOADDOCUMENT));
                URL fileUrl = new URL(execute.getFileUrl(token));
                HttpURLConnection httpConn = (HttpURLConnection)fileUrl.openConnection();
                InputStream inputStream = httpConn.getInputStream();
                byte[] output = IOUtils.toByteArray(inputStream);
                FileOutputStream stream = new FileOutputStream(file);
                stream.write(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
            send(documentInfraService.addSubCollection(file, update));
        }
    }

    /**
     * Метод @Scheduled, проверяет каждый день в 17:00 должен ли кто нибудь завтра сдать книную,
     * если находит таких - > отправляет напоминание
     */
    @Scheduled(cron = "0 0 17 * * ?")
    private void sendPlanMessages() {
//        List<BotApiMethod> apiMethod = botService.getPlaningMessage();
        List<BotApiMethod> apiMethod = new ArrayList<>();
        for (BotApiMethod messageBotApiMethod : apiMethod) {
            try {
                if (messageBotApiMethod != null) {
                    execute(messageBotApiMethod);
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(SendMessage sendMessage) {
        try {
            if (sendMessage != null) {
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(BotApiMethod apiMethod) {
        try {
            execute(apiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(List<BotApiMethod> apiMethod) {
        try {
            for (BotApiMethod messageBotApiMethod : apiMethod) {
                if (messageBotApiMethod != null) {
                    execute(messageBotApiMethod);
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
