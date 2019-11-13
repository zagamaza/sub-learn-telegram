package ru.maza.telegram.infra.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.infra.service.BotInfraService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class BotInfraServiceImpl implements BotInfraService {

    private Map<Integer, String> supportMap = new HashMap<>();

    private final BotService botService;

    public BotInfraServiceImpl(
            BotService botService
    ) {
        this.botService = botService;
        init(supportMap);
    }

    private void init(Map<Integer, String> supportMap) {
        supportMap.put(1, "start1");
        supportMap.put(2, "collections");
        supportMap.put(3, "base");
        supportMap.put(4, "search1");
        supportMap.put(5, "search_second");
        supportMap.put(6, "add_collection");
        supportMap.put(7, "serial");
        supportMap.put(8, "season");
        supportMap.put(9, "series");
        supportMap.put(10, "translation");
        supportMap.put(11, "start2");
        supportMap.put(12, "trial");
        supportMap.put(13, "start3");
        supportMap.put(14, "settin");
    }

    @Override
    public List<BotApiMethod> getStartWindow(Update update, Boolean isEdit) {
        if (isEdit) {
            return botService.getEditMessageForStart(update);
        } else { return botService.getMessageForStart(update); }
    }

    @Override
    public SendPhoto getInfoMessages(Integer supportId, Update update) {
        if (supportId > 14) {
            supportId = 1;
        }
        String support = supportMap.get(supportId);
        return botService.getMessageSupport(supportId, support, update);
    }

}
