package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.WordDto;


@FeignClient(contextId = "words", name = "words", url = "${sublearn.back.url}")
public interface WordClientApi {

    @PutMapping("/words")
    WordDto update(@RequestBody WordDto dto);

    @GetMapping("/words/{id}")
    WordDto getWord(@PathVariable("id") Long id);

}
