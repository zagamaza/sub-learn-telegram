package ru.maza.telegram.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maza.telegram.dto.WordDto;

@FeignClient(contextId = "translator", name = "translator", url = "localhost:8080/")
public interface TranslatorClientApi {

    @GetMapping("/result")
    WordDto translate(@RequestParam("word") String word);

}
