package ru.maza.telegramweb.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegramweb.dto.WordDto;


@FeignClient(contextId = "words", name = "words", url = "localhost:8082/api")
public interface WordClientApi {

    @PutMapping("/words")
    WordDto update(@RequestBody WordDto dto);

    @GetMapping("/words/{id}")
    WordDto getWord(@PathVariable("id") Long id);

}
