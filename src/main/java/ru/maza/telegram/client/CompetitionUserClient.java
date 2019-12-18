package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.competition.CompetitionUserDto;

@FeignClient(contextId = "userCompetitions", name = "userCompetitions", url = "${sublearn.competition.url}")
public interface CompetitionUserClient {

    @GetMapping("/users/{id}")
    CompetitionUserDto get(@PathVariable("id") Long id);

    @GetMapping("/users/user_friends/{userId}")
    RestPageImpl<CompetitionUserDto> getByUserFriendUserId(@PathVariable("userId") Long userId, Pageable pageable);

    @GetMapping("/users/telegram/{telegramId}")
    CompetitionUserDto getByTelegramId(@PathVariable Long telegramId);

    @PostMapping("/users")
    CompetitionUserDto create(@RequestBody CompetitionUserDto competitionUserDto);

    @PutMapping("/users")
    CompetitionUserDto update(@RequestBody CompetitionUserDto competitionUserDto);

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable("id") Long id);

}
