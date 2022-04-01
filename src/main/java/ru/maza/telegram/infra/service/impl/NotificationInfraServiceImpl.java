package ru.maza.telegram.infra.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.client.NotificationClient;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.dto.NotificationDto;
import ru.maza.telegram.dto.NotificationType;
import ru.maza.telegram.infra.service.NotificationInfraService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class NotificationInfraServiceImpl implements NotificationInfraService {

    private final NotificationClient notificationClient;
    private final BotService botService;

    private AnswerCallbackQuery answerCallbackQuery;

    @Value("${notification.count}")
    private Integer notificationCount;

    @Override
    public List<BotApiMethod> getTextNotifications() {
        RestPageImpl<NotificationDto> notifications = notificationClient.getNotificationsByType(
                NotificationType.MESSAGE,
                PageRequest.of(0, notificationCount, Sort.by("created").ascending())
        );
        if (notifications.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> notificationsIds = notifications.stream()
                                                   .map(NotificationDto::getId)
                                                   .collect(Collectors.toList());
        notificationClient.deleteByIds(notificationsIds);
        return botService.getMessageNotifications(notifications.getContent());
    }

    @Override
    public void updateCallbackNotifications() {
        notificationClient.getNotificationsByType(NotificationType.CALLBACK, PageRequest.of(0, notificationCount))
                          .getContent()
                          .stream()
                          .findFirst()
                          .ifPresent(n -> answerCallbackQuery = AnswerCallbackQuery.builder().text(n.getText()).build());
    }

    @Override
    public AnswerCallbackQuery getRandomAnswerCallback(Update update) {
        int i = (int)(Math.random() * 500);
        if (i == 77) {
            AnswerCallbackQuery callbackQuery = getAnswerCallbackQuery();
            if (callbackQuery == null) {
                return null;
            }
            callbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            callbackQuery.setShowAlert(true);
            return callbackQuery;
        } else { return null; }
    }

    @Override
    public AnswerCallbackQuery getAnswerCallback() {
        return getAnswerCallbackQuery();
    }

}
