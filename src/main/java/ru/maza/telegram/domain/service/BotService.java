package ru.maza.telegram.domain.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.NotificationDto;
import ru.maza.telegram.dto.UserDto;

import java.util.List;

public interface BotService {

    List<BotApiMethod> getMessageForStart(Update update);

    List<BotApiMethod> fillMessageHowYouAddCollection(Update update);

    List<BotApiMethod> getEditMessageForStart(Update update);

    List<BotApiMethod> getExceptionMessage(Update update);

    SendPhoto getMessageSupport(Integer supportId, String support, Update update);

    List<BotApiMethod> getMessageDocumentNotExists(Long commandId, UserDto userDto, Update update);

    List<BotApiMethod> getMessageNotifications(List<NotificationDto> content);

    BotApiMethod<Boolean> getAlertForResetProgress(Update update);

}
