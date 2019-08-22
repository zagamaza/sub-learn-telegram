package ru.maza.telegramweb.infra.client;

//import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.maza.telegramweb.dto.TranslateOptionDto;
import ru.maza.telegramweb.dto.TrialCondensedDto;
import ru.maza.telegramweb.dto.TrialDto;
import ru.maza.telegramweb.dto.TrialRequestDto;

import java.util.List;


@FeignClient(contextId = "trials", name = "trials", url = "localhost:8082/api")
public interface TrialClientApi {


    @GetMapping("/trials/{id}")
    TrialDto get(@PathVariable("id") Long id);

    @GetMapping("/trials/condensed/users/{userId}")
    List<TrialCondensedDto> getLastConsedTrial(@PathVariable("userId") Long userId);

    @PostMapping("/trials")
    TrialDto createTrial(@RequestBody TrialRequestDto dto);

    @GetMapping("/trials/nextWord")
    TranslateOptionDto getNextWord(@RequestParam("trialId") Long trialId);

}
