package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maza.telegram.dto.Lang;
import ru.maza.telegram.dto.WordDto;

@FeignClient(contextId = "translator", name = "translator", url = "imbir.ga:8081/api/translator")
public interface TranslatorClientApi {

    @GetMapping
    WordDto translate(@RequestParam("word") String word, @RequestParam("lang") Lang lang);

}
