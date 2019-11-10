package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import ru.maza.telegram.client.TranslatorClientApi;
import ru.maza.telegram.client.WordClientApi;
import ru.maza.telegram.client.impl.CollectionClient;
import ru.maza.telegram.domain.service.CallbackService;
import ru.maza.telegram.domain.service.TelegramService;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.Lang;
import ru.maza.telegram.dto.WordDto;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.infra.service.CallbackInfraService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallbackInfraServiceImpl implements CallbackInfraService {

    private final CallbackService callbackService;

    private final TelegramService telegramService;
    private final TranslatorClientApi translatorClientApi;
    private final WordClientApi wordClientApi;
    private final CollectionClient collectionClient;

    @Override
    public CallbackData getCallbackData(String data) {
        return callbackService.getCallbackData(data);
    }

    @Override
    public List<BotApiMethod> deleteMessage(Update update) {
        return List.of(telegramService.deleteMessage(update));
    }

    @Override
    public List<BotApiMethod> getTranscription(Long wordId, Update update) {
        WordDto word = wordClientApi.getWord(wordId);
        if (word.getTranscription() == null) {
            word = translatorClientApi.translate(word.getWord(), Lang.EN_RU);
        }
        return callbackService.fillAnswerAlertForWord(word, update);
    }

    @Override
    public List<BotApiMethod> searchCollection(Update update) {
        String queryText = update.getInlineQuery().getQuery();
        AnswerInlineQuery answerInlineQuery = telegramService.getAnswerInlineQuery(update);
        List<CollectionCondensedDto> collections = collectionClient.search(queryText, PageRequest.of(0, 10));
        List<InlineQueryResult> results = collections
                .stream()
                .map(telegramService::fillInlineQueryResultPhoto)
                .collect(Collectors.toList());
        answerInlineQuery.setResults(results);
        return Collections.singletonList(answerInlineQuery);
    }

}
