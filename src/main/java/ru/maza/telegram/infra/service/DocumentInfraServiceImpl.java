package ru.maza.telegram.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.maza.telegram.domain.service.CallbackService;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.infra.client.CollectionClientApi;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentInfraServiceImpl implements DocumentInfraService {

    private final CallbackService callbackService;
    private final CollectionClientApi collectionClientApi;


    @Override
    public List<BotApiMethod> addSubCollection(File file, Update update) {
        String fileName = update.getMessage().getDocument().getFileName().split("\\.")[0];
        CollectionDto collectionDto = uploadFileAndGetCollection(file);
        collectionDto.setName(fileName);
        collectionDto = collectionClientApi.save(collectionDto);
        return callbackService.afterSaveCollection(collectionDto, update);
    }

    private CollectionDto uploadFileAndGetCollection(File file) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(file);
        map.add("file", value);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CollectionDto> response = restTemplate.exchange(
                "http://localhost:8080/result",
                HttpMethod.POST,
                requestEntity,
                CollectionDto.class
        );
        return response.getBody();

    }

}
