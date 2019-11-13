package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.TranslateOptionDto;
import ru.maza.telegram.dto.TrialCondensedDto;
import ru.maza.telegram.dto.TrialDto;
import ru.maza.telegram.dto.TrialRequest;

import java.util.List;


@FeignClient(contextId = "trials", name = "trials", url = "${sublearn.back.url}")
public interface TrialClientApi {

    @GetMapping("/trials/{id}")
    TrialDto get(@PathVariable("id") Long id);

    @GetMapping("/trials")
    List<TrialDto> getAll(Pageable pageable);

    @GetMapping("/trials/condensed/users/{userId}")
    RestPageImpl<TrialCondensedDto> getLastConsedTrial(@PathVariable("userId") Long userId, Pageable pageable);

    @GetMapping("/trials/users/{userId}/episodes/{episodeId}")
    TrialDto getLastNotFinishTrialByEpisodeIdAndUserId(
            @PathVariable("userId") Long userId,
            @PathVariable("episodeId") Long episodeId
    );

    @GetMapping("/trials/nextWord")
    TranslateOptionDto getTranslateOptionDto(@RequestParam("trialId") Long trialId);

    @PostMapping("/trials/trial_word")
    TrialDto saveTrialAnd20TrialWord(@RequestBody TrialRequest trialRequest);

    @PostMapping("/trials")
    TrialDto create(@RequestBody TrialRequest trialRequest);

    @PutMapping("/trials")
    TrialDto update(@RequestBody TrialRequest trialRequest);

    @DeleteMapping("/trials/{id}")
    void delete(@PathVariable("id") Long id);

}
