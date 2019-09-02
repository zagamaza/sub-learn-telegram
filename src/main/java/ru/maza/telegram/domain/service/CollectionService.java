package ru.maza.telegram.domain.service;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.dto.CollectionRequest;
import ru.maza.telegram.dto.Lang;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CollectionService {
    private final TelegramService telegramService;

    public CollectionRequest fillCollectionRequest(Update update) {
        return CollectionRequest.builder()
                .created(LocalDateTime.now())
                .name(telegramService.getTextMessage(update))
                .isSerial(false)
                .lang(Lang.RU_US)
                .userId(telegramService.getUserId(update).longValue())
                .build();
    }
}
