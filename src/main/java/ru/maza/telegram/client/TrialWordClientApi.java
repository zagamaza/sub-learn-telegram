package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.TrialWordDto;
import ru.maza.telegram.dto.TrialWordRequest;


@FeignClient(contextId = "trialWords", name = "trialWords", url = "${sublearn.back.url}")
public interface TrialWordClientApi {

    @GetMapping("/trial_words/{id}")
    TrialWordDto get(@PathVariable("id") Long id);

    @PostMapping("/trial_words")
    TrialWordDto save(@RequestBody TrialWordRequest trialWordRequest);

    @PutMapping("/trial_words")
    TrialWordDto update(@RequestBody TrialWordRequest trialWordRequest);

    @PutMapping("/trial_words/user_word")
    TrialWordDto updateTrialWordAndSaveUserWord(@RequestBody TrialWordRequest trialWordRequest);

    @GetMapping("/trial_words/{id}/learned")
    TrialWordDto updateTrialWordAndSaveLearnedUserWord(@PathVariable ("id") Long id);

    @DeleteMapping("/trial_words/{id}")
    void delete(@PathVariable("id") Long id);

}
