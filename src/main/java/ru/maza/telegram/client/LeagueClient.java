package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.competition.LeagueDto;
import ru.maza.telegram.dto.competition.Level;

import javax.validation.Valid;

@FeignClient(contextId = "leagues", name = "leagues" +
        "", url = "${sublearn.competition.url}")
public interface LeagueClient {

    @GetMapping("/leagues/{id}")
    LeagueDto get(@PathVariable("id") Integer id);

    @GetMapping("/leagues")
    RestPageImpl<LeagueDto> getByLeagueLevelCode(@RequestParam("level") Level level, Pageable pageable);

    @PostMapping("/leagues")
    LeagueDto create(@RequestBody LeagueDto leagueDto);

    @PutMapping("/leagues")
    LeagueDto update(@RequestBody LeagueDto leagueDto);

    @DeleteMapping("/leagues/{id}")
    void delete(@PathVariable("id") Integer id);

}
