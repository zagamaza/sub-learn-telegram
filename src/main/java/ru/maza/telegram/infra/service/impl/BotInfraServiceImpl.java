package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.infra.service.BotInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotInfraServiceImpl implements BotInfraService {

    private final BotService botService;

    @Override
    public List<BotApiMethod> getStartWindow(Update update, Boolean isEdit) {
        if (isEdit) {
            return botService.getEditMessageForStart(update);
        } else { return botService.getMessageForStart(update); }
    }

}
