package ru.maza.telegram.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.AddCollectionCD;
import ru.maza.telegram.dto.callbackData.AddFileCD;
import ru.maza.telegram.dto.callbackData.AddPersonalCollectionCD;
import ru.maza.telegram.dto.callbackData.AddSearchCollectionCD;
import ru.maza.telegram.dto.callbackData.CTlteCD;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.CancelCD;
import ru.maza.telegram.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;
import ru.maza.telegram.dto.callbackData.ChooseSeasonCD;
import ru.maza.telegram.dto.callbackData.ChooseStartTrialCD;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;
import ru.maza.telegram.dto.callbackData.ChsSeriesCD;
import ru.maza.telegram.dto.callbackData.DelCollectionCD;
import ru.maza.telegram.dto.callbackData.LearnedWordCD;
import ru.maza.telegram.dto.callbackData.MyCollectionsCD;
import ru.maza.telegram.dto.callbackData.MySettingsCD;
import ru.maza.telegram.dto.callbackData.MyTrialsCD;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.dto.callbackData.PageSeriesCD;
import ru.maza.telegram.dto.callbackData.ScheduleCD;
import ru.maza.telegram.dto.callbackData.ShowAllTrCD;
import ru.maza.telegram.dto.callbackData.TranscriptionCD;
import ru.maza.telegram.dto.callbackData.TranslateCountCD;
import ru.maza.telegram.dto.callbackData.WordCountCD;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.service.BotInfraService;
import ru.maza.telegram.infra.service.CallbackInfraService;
import ru.maza.telegram.infra.service.CollectionInfraService;
import ru.maza.telegram.infra.service.CommandInfraService;
import ru.maza.telegram.infra.service.DocumentInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;
import ru.maza.telegram.infra.service.TextInfraService;
import ru.maza.telegram.infra.service.TrialInfraService;
import ru.maza.telegram.infra.service.UserSettingInfraService;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BotController extends TelegramLongPollingBot {

    private static final String NEW_COLLECTION = "/new_sub";
    private static final String EDIT_COLLECTION = "/edit_sub";
    private static final String ADD_FILE = "/add_file";

    private final TelegramService telegramService;
    private final DocumentInfraService documentInfraService;
    private final TextInfraService textInfraService;
    private final CallbackInfraService callbackInfraService;
    private final CommandInfraService commandInfraService;

    private final BotInfraService botInfraService;
    private final CollectionInfraService collectionInfraService;
    private final EpisodeInfraService episodeInfraService;
    private final TrialInfraService trialInfraService;
    private final UserSettingInfraService userSettingInfraService;

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
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        Integer userId = telegramService.getUser(update).getId();
        UserDto userDto = textInfraService.saveUser(update, userId);

        if (update.hasMessage()) {
            Command command = commandInfraService.get(userDto.getId());
            commandInfraService.remove(userDto.getId());
            if (command != null) {
                switch (command.getTextCommands()) {
                    case (NEW_COLLECTION):
                        send(collectionInfraService.createCollection(userDto, update));
                        break;
                    case (EDIT_COLLECTION):
                        send(collectionInfraService.createCollection(userDto, update));
                        break;
                    case (ADD_FILE):
                        send(saveFile(update, command.getCommandId()));
                        break;
                    case ("/add_season"):
                        send(episodeInfraService.addSeason(command.getCommandId(), userDto, update));
                        break;
                    case ("/add_serial"):
                        send(episodeInfraService.addSeries(command.getCommandId(), userDto, update));
                        break;
                }
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String answer = telegramService.getTextMessage(update).toLowerCase();
            switch (answer) {
                case ("/start"):
                    send(botInfraService.getStartWindow(update, false));
                    break;
            }

            /**Пересланное сообщение*/
            if (update.getMessage().getReplyToMessage() != null && update.getMessage().getReplyToMessage().hasText()) {
                answer = update.getMessage().getReplyToMessage().getText().toLowerCase();
                switch (answer) {
                    case ("ss"):
                        send(collectionInfraService.createCollection(userDto, update));
                        break;
                }
            }

        }
        if (update.hasCallbackQuery()) {
            CallbackData callbackData = callbackInfraService.getCallbackData(update.getCallbackQuery().getData());

            if (callbackData instanceof ChooseCollectionCD) {
                ChooseCollectionCD chooseCollection = (ChooseCollectionCD)callbackData;
                send(collectionInfraService.chooseCollection(chooseCollection.getCltnId(), userDto, update));
            } else if (callbackData instanceof CTlteCD) {
                CTlteCD chooseTranslateCD = (CTlteCD)callbackData;
                List<BotApiMethod> botApiMethods = trialInfraService.saveResult(chooseTranslateCD, update);
                send(botApiMethods);
                if (botApiMethods.get(0) instanceof AnswerCallbackQuery) {
                    send(trialInfraService.repeatTrial(chooseTranslateCD.getTl(), userDto, update));
                }
                if (!chooseTranslateCD.getRw().equals(chooseTranslateCD.getWd()) ||
                        userDto.getUserSettingDto().isShowAllTranslate()) {
                    send(trialInfraService.getAlertWithAllTranslates(chooseTranslateCD, update));
                }
                Thread.sleep(500);
                send(trialInfraService.getNextWord(chooseTranslateCD.getTl(), update));
            } else if (callbackData instanceof CancelCD) {
                CancelCD cancelCD = (CancelCD)callbackData;
                if ("/start".equals(cancelCD.getCommand())) {
                    send(botInfraService.getStartWindow(update, true));
                } else if ("/my_collection".equals(cancelCD.getCommand())) {
                    send(collectionInfraService.getAllCollection(userDto, update));
                }
            } else if (callbackData instanceof TranscriptionCD) {
                TranscriptionCD transcriptionCD = (TranscriptionCD)callbackData;
                send(callbackInfraService.getTranscription(transcriptionCD.getWordId(), update));
            } else if (callbackData instanceof ChooseTrialCD) {
                ChooseTrialCD chooseTrialCD = (ChooseTrialCD)callbackData;
                send(trialInfraService.chooseTrial(chooseTrialCD, update));
            } else if (callbackData instanceof ChooseIsSerialCD) {
                ChooseIsSerialCD chooseIsSerialCD = (ChooseIsSerialCD)callbackData;
                send(collectionInfraService.setIsSerialCollection(chooseIsSerialCD, update, userDto));
            } else if (callbackData instanceof MyCollectionsCD) {
                send(collectionInfraService.getAllCollection(userDto, update));
            } else if (callbackData instanceof AddPersonalCollectionCD) {
                AddPersonalCollectionCD addPersonalCollectionCD = (AddPersonalCollectionCD)callbackData;
                if (addPersonalCollectionCD.getCtnId() == null && addPersonalCollectionCD.getEpdId() == null) {
                    commandInfraService.save(new Command(userDto.getId(), NEW_COLLECTION, null));
                    send(collectionInfraService.wantCreatePersonalCollection(userDto, update));
                } else if (addPersonalCollectionCD.getCtnId() != null && addPersonalCollectionCD.getEpdId() == null) {
                    send(episodeInfraService.createSerial(addPersonalCollectionCD.getCtnId(), update, userDto));
                } else {
                    commandInfraService.save(new Command(addPersonalCollectionCD.getCtnId(), "add_serial", null));
                    send(episodeInfraService.wantToCreateSeries(addPersonalCollectionCD.getEpdId(), userDto, update));
                }
            } else if (callbackData instanceof AddCollectionCD) {
                send(collectionInfraService.wantCreateCollection(userDto, update));
            } else if (callbackData instanceof ChooseStartTrialCD) {
                ChooseStartTrialCD chooseStartTrialCD = (ChooseStartTrialCD)callbackData;
                if (chooseStartTrialCD.getTrialId() != null) {
                    send(trialInfraService.getNextWord(chooseStartTrialCD.getTrialId(), update));
                } else { send(trialInfraService.startTrial(userDto, update, chooseStartTrialCD.getEpisodeId())); }
            } else if (callbackData instanceof MyTrialsCD) {
                send(trialInfraService.getAllTrials(userDto, update));
            } else if (callbackData instanceof MySettingsCD) {
                send(userSettingInfraService.getMySettings(userDto, update));
            } else if (callbackData instanceof TranslateCountCD) {
                TranslateCountCD translateCountCD = (TranslateCountCD)callbackData;
                send(userSettingInfraService.updateTranslateCount(userDto, translateCountCD, update));
            } else if (callbackData instanceof WordCountCD) {
                WordCountCD wordCountCD = (WordCountCD)callbackData;
                send(userSettingInfraService.updateWordCountInTrial(userDto, wordCountCD, update));
            } else if (callbackData instanceof ScheduleCD) {
                send(userSettingInfraService.updateSchedule(userDto, update));
            } else if (callbackData instanceof ShowAllTrCD) {
                send(userSettingInfraService.updateShowAllTranslate(userDto, update));
            } else if (callbackData instanceof AddSearchCollectionCD) {
                AddSearchCollectionCD searchCollectionCD = (AddSearchCollectionCD)callbackData;
                send(collectionInfraService.addCollection(userDto, searchCollectionCD.getClctnId(), update));
            } else if (callbackData instanceof DelCollectionCD) {
                DelCollectionCD delCollectionCD = (DelCollectionCD)callbackData;
                send(collectionInfraService.deleteCollection(userDto, delCollectionCD.getCollectionId(), update));
            } else if (callbackData instanceof PageCD) {
                PageCD pageCD = (PageCD)callbackData;
                if (pageCD.getEntity().equals("collection")) {
                    send(collectionInfraService.getCollectionByPage(userDto, pageCD, update));
                } else if (pageCD.getEntity().equals("trial")) {
                    send(trialInfraService.getTrialsByPage(userDto, pageCD, update));
                }
            } else if (callbackData instanceof LearnedWordCD) {
                LearnedWordCD learnedWordCD = (LearnedWordCD)callbackData;
                List<BotApiMethod> botApiMethods = trialInfraService.saveLearnedTrialWordAndGetNextWord(
                        learnedWordCD.getTwId(),
                        update
                );
                send(botApiMethods);
                if (botApiMethods.get(0) instanceof AnswerCallbackQuery) {
                    send(trialInfraService.repeatTrial(learnedWordCD.getTlId(), userDto, update));
                }
            } else if (callbackData instanceof ChooseSeasonCD) {
                ChooseSeasonCD chooseSeasonCD = (ChooseSeasonCD)callbackData;
                send(episodeInfraService.chooseSeason(chooseSeasonCD, userDto, update));
            } else if (callbackData instanceof ChsSeriesCD) {
                ChsSeriesCD chsSeriesCD = (ChsSeriesCD)callbackData;
                send(episodeInfraService.chooseSeries(chsSeriesCD, userDto, update));
            } else if (callbackData instanceof PageSeriesCD) {
                PageSeriesCD pageSeriesCD = (PageSeriesCD)callbackData;
                send(episodeInfraService.getSerialsByPage(userDto, pageSeriesCD, update));
            } else if (callbackData instanceof AddFileCD) {
                AddFileCD addFileCD = (AddFileCD)callbackData;
                send(episodeInfraService.addFile(addFileCD.getEpisodeId(), userDto, update));
            }


            /**
             * Работа с Emoji для перелистывания между страницами
             */
//                else if (EmojiManager.isEmoji(data.replaceAll("[0-9]", ""))) {
//                    Emoji emoji = EmojiManager.getByUnicode(data.replaceAll("[0-9]", ""));
//                }
        }
        if (update.hasInlineQuery()) {
            send(callbackInfraService.searchCollection(update));
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

    private List<BotApiMethod> saveFile(Update update, Long episodeId) {
        GetFile getFile = new GetFile();
        getFile.setFileId(update.getMessage().getDocument().getFileId());
        java.io.File file = null;

        try {
            file = java.io.File.createTempFile("file", null);
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
        return documentInfraService.addSubEpisode(file, episodeId, update);
    }


}
