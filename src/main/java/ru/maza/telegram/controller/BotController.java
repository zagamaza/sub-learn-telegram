package ru.maza.telegram.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.maza.telegram.client.UserActionClient;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.ActionType;
import ru.maza.telegram.dto.Constant;
import ru.maza.telegram.dto.UserActionDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.callbackData.AddCollectionCD;
import ru.maza.telegram.dto.callbackData.AddFileCD;
import ru.maza.telegram.dto.callbackData.AddFriendCD;
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
import ru.maza.telegram.dto.callbackData.DelEpisodeCD;
import ru.maza.telegram.dto.callbackData.DeleteFriendCD;
import ru.maza.telegram.dto.callbackData.LearnedWordCD;
import ru.maza.telegram.dto.callbackData.LearnedWordCountCD;
import ru.maza.telegram.dto.callbackData.MyCollectionsCD;
import ru.maza.telegram.dto.callbackData.MyCompetitionsCD;
import ru.maza.telegram.dto.callbackData.MyLeagueCD;
import ru.maza.telegram.dto.callbackData.MySettingsCD;
import ru.maza.telegram.dto.callbackData.MyTrialsCD;
import ru.maza.telegram.dto.callbackData.PageCD;
import ru.maza.telegram.dto.callbackData.PageSeriesCD;
import ru.maza.telegram.dto.callbackData.ScheduleCD;
import ru.maza.telegram.dto.callbackData.ShowAllTrCD;
import ru.maza.telegram.dto.callbackData.SupportCD;
import ru.maza.telegram.dto.callbackData.TranscriptionCD;
import ru.maza.telegram.dto.callbackData.TranslateCountCD;
import ru.maza.telegram.dto.callbackData.WordCountCD;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.service.BotInfraService;
import ru.maza.telegram.infra.service.CallbackInfraService;
import ru.maza.telegram.infra.service.CollectionInfraService;
import ru.maza.telegram.infra.service.CommandInfraService;
import ru.maza.telegram.infra.service.CompetitionInfraService;
import ru.maza.telegram.infra.service.DocumentInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;
import ru.maza.telegram.infra.service.LeagueInfraService;
import ru.maza.telegram.infra.service.TextInfraService;
import ru.maza.telegram.infra.service.TrialInfraService;
import ru.maza.telegram.infra.service.UserSettingInfraService;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Component
public class BotController extends TelegramLongPollingBot {

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
    private final CompetitionInfraService competitionInfraService;
    private final LeagueInfraService leagueInfraService;

    private final UserActionClient userActionClient;

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
                userActionClient.create(new UserActionDto(command.getTextCommands(), userDto.getId(), ActionType.COMMAND));

                switch (command.getTextCommands()) {
                    case (Constant.NEW_COLLECTION):
                        send(collectionInfraService.createCollection(userDto, update));
                        break;
                    case (Constant.EDIT_COLLECTION):
                        send(collectionInfraService.createCollection(userDto, update));
                        break;
                    case (Constant.ADD_FILE):
                        List<BotApiMethod> botApiMethods = documentInfraService.checkDocument(userDto, command.getCommandId(), update);
                        send(botApiMethods);
                        if (isEmpty(botApiMethods)) { send(saveFile(update, command.getCommandId())); }
                        break;
                    case (Constant.ADD_SEASON):
                        send(episodeInfraService.addSeason(command.getCommandId(), userDto, update));
                        break;
                    case (Constant.ADD_SERIAL):
                        send(episodeInfraService.addSeries(command.getCommandId(), userDto, update));
                        break;
                    case (Constant.ADD_FRIEND):
                        send(competitionInfraService.addFriend(userDto, update));
                        send(competitionInfraService.getCompetitionsWindow(userDto, update, false));
                        break;
                    case (Constant.DELETE_FRIEND):
                        send(competitionInfraService.deleteFriend(userDto, update));
                        send(competitionInfraService.getCompetitionsWindow(userDto, update, false));
                        break;
                }
            }
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            userActionClient.create(new UserActionDto(update.getMessage().getText(), userDto.getId(), ActionType.TEXT));

            String answer = telegramService.getTextMessage(update).toLowerCase();
            switch (answer) {
                case ("/start"):
                    send(botInfraService.getStartWindow(update, false));
                    break;
                case ("/help"):
                    send(botInfraService.getInfoMessages(1, update));
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
            String data = update.getCallbackQuery().getData();

            CallbackData callbackData = callbackInfraService.getCallbackData(data);
            userActionClient.create(new UserActionDto(callbackData.getCz(), userDto.getId(), ActionType.CALLBACK));

            if (callbackData instanceof ChooseCollectionCD) {
                ChooseCollectionCD chooseCollection = (ChooseCollectionCD)callbackData;
                send(collectionInfraService.chooseCollection(chooseCollection.getCltnId(), userDto, update));
            } else if (callbackData instanceof CTlteCD) {
                CTlteCD chooseTranslateCD = (CTlteCD)callbackData;
                send(trialInfraService.saveAndCheckResult(chooseTranslateCD, userDto, update));
                if (!chooseTranslateCD.getRw().equals(chooseTranslateCD.getWd()) ||
                        userDto.getUserSettingDto().isShowAllTranslate()) {
                    Thread.sleep(100);
                    send(trialInfraService.getAlertWithAllTranslates(chooseTranslateCD.getRw(), update));
                }
                List<BotApiMethod> nextWord = trialInfraService.getNextWord(chooseTranslateCD.getTl(), update);
                Thread.sleep(500);
                if (nextWord.get(0) instanceof AnswerCallbackQuery) {
                    send(trialInfraService.repeatTrial(chooseTranslateCD.getTl(), userDto, update));
                }
                send(nextWord);
            } else if (callbackData instanceof CancelCD) {
                CancelCD cancelCD = (CancelCD)callbackData;
                if ("/start".equals(cancelCD.getCommand())) {
                    send(botInfraService.getStartWindow(update, true));
                } else if (Constant.MY_COLLECTION.equals(cancelCD.getCommand())) {
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
                    commandInfraService.save(new Command(userDto.getId(), Constant.NEW_COLLECTION, null));
                    send(collectionInfraService.wantCreatePersonalCollection(userDto, update));
                } else if (addPersonalCollectionCD.getCtnId() != null && addPersonalCollectionCD.getEpdId() == null) {
                    send(episodeInfraService.createSerial(addPersonalCollectionCD.getCtnId(), update, userDto));
                } else {
                    commandInfraService.save(new Command(
                            addPersonalCollectionCD.getCtnId(),
                            Constant.ADD_SERIAL,
                            null
                    ));
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
            }

            /**Страница настроек*/
            else if (callbackData instanceof MySettingsCD) {
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
            } else if (callbackData instanceof LearnedWordCountCD) {
                LearnedWordCountCD learnedWordCountCD = (LearnedWordCountCD)callbackData;
                send(userSettingInfraService.updateLearnedWordCount(learnedWordCountCD.getLearnedCount(), userDto, update));
            }

            /**Страница рейтинга*/
            else if (callbackData instanceof MyCompetitionsCD) {
                send(competitionInfraService.getCompetitionsWindow(userDto, update, true));
            }  else if (callbackData instanceof AddFriendCD) {
                send(competitionInfraService.wantAddFriend(userDto, update));
            } else if (callbackData instanceof MyLeagueCD) {
                send(leagueInfraService.getLeagueUsersWindow(userDto, update));
            }else if (callbackData instanceof DeleteFriendCD) {
                send(competitionInfraService.wantDeleteFriend(userDto, update));
            }





            else if (callbackData instanceof AddSearchCollectionCD) {
                AddSearchCollectionCD searchCollectionCD = (AddSearchCollectionCD)callbackData;
                send(collectionInfraService.addCollection(userDto, searchCollectionCD.getClctnId(), update));
            } else if (callbackData instanceof DelCollectionCD) {
                DelCollectionCD delCollectionCD = (DelCollectionCD)callbackData;
                send(collectionInfraService.deleteCollection(userDto, delCollectionCD.getCollectionId(), update));
            } else if (callbackData instanceof DelEpisodeCD) {
                DelEpisodeCD delEpisodeCD = (DelEpisodeCD)callbackData;
                send(episodeInfraService.deleteEpisode(userDto, delEpisodeCD.getEpisodeId(), update));
            } else if (callbackData instanceof PageCD) {
                PageCD pageCD = (PageCD)callbackData;
                if (pageCD.getEntity().equals("collection")) {
                    send(collectionInfraService.getCollectionByPage(userDto, pageCD, update));
                } else if (pageCD.getEntity().equals("trial")) {
                    send(trialInfraService.getTrialsByPage(userDto, pageCD, update));
                } else if (pageCD.getEntity().equals("friend")) {
                    send(competitionInfraService.getFriendsByPage(userDto, pageCD, update));
                }else if (pageCD.getEntity().equals("league")) {
                    send(leagueInfraService.getLeagueUsersByPage(userDto, pageCD, update));
                }
            } else if (callbackData instanceof LearnedWordCD) {
                LearnedWordCD learnedWordCD = (LearnedWordCD)callbackData;
                send(trialInfraService.getAlertWithAllTranslates(learnedWordCD.getWdId(), update));
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
            } else if (callbackData instanceof SupportCD) {
                SupportCD supportCD = (SupportCD)callbackData;
                SendPhoto sendPhoto = botInfraService.getInfoMessages(supportCD.getSupportId(), update);
                send(new DeleteMessage(
                        telegramService.getChatId(update),
                        telegramService.getMessage(update).getMessageId()
                ));
                send(sendPhoto);
            }


            /**
             * Работа с Emoji для перелистывания между страницами
             */
//                else if (EmojiManager.isEmoji(data.replaceAll("[0-9]", ""))) {
//                    Emoji emoji = EmojiManager.getByUnicode(data.replaceAll("[0-9]", ""));
//                }
        }
        if (update.hasInlineQuery()) {
            String query = update.getInlineQuery().getQuery();
            userActionClient.create(new UserActionDto(query, userDto.getId(), ActionType.INLINE_QUERY));
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

    private void send(SendPhoto apiMethod) {
        try {
            execute(apiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void send(SendMediaGroup apiMethod) {
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
            send(telegramService.getSendChatAction(
                    telegramService.getMessage(update),
                    org.telegram.telegrambots.meta.api.methods.ActionType.UPLOADDOCUMENT
            ));
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
