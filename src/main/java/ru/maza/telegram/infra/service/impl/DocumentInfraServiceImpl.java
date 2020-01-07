package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import ru.maza.telegram.domain.service.BotService;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.infra.service.DocumentInfraService;
import ru.maza.telegram.infra.service.EpisodeInfraService;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentInfraServiceImpl implements DocumentInfraService {

    private final EpisodeInfraService episodeInfraService;
    private final RestTemplate restTemplate;
    private final BotService botService;

    @Value("${sublearn.back.url}")
    private String sublearnBackUrl;

    @Override
    public List<BotApiMethod> addSubEpisode(File file, Long episodeId, Update update) {
        EpisodeDto episodeDto = uploadFileAndGetEpisode(file, episodeId);
        return episodeInfraService.afterSaveSub(episodeDto, update);
    }

    @Override
    public List<BotApiMethod> checkDocument(UserDto userDto, Long commandId, Update update) {
        if (!update.getMessage().hasDocument() ||
                (!update.getMessage().getDocument().getMimeType().equals("application/x-subrip") &&
                        !update.getMessage().getDocument().getMimeType().equals("application/binary"))) {
            return botService.getMessageDocumentNotExists(commandId, userDto, update);
        }
        return Collections.emptyList();
    }

    private EpisodeDto uploadFileAndGetEpisode(File file, Long episodeID) {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        FileSystemResource value = new FileSystemResource(file);
        map.add("file", value);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        ResponseEntity<EpisodeDto> response = restTemplate.exchange(
                sublearnBackUrl + "/episodes/" + episodeID,
                HttpMethod.PUT,
                requestEntity,
                EpisodeDto.class
        );
        return response.getBody();

    }

}
