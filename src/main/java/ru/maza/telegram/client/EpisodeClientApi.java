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
import org.springframework.web.multipart.MultipartFile;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.EpisodeRequest;

import java.util.List;

@FeignClient(contextId = "episodes", name = "episodes", url = "${sublearn.back.url}")
public interface EpisodeClientApi {

    @GetMapping("/episodes/{id}")
    EpisodeDto get(@PathVariable("id") Long id);

    @GetMapping("/episodes/collections/{collectionId}")
    RestPageImpl<EpisodeDto> getAllByCollectionId(@PathVariable("collectionId") Long collectionId, Pageable pageable);

    @GetMapping("/episodes/{id}/users/{userId}")
    Integer getLearnedPercent(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @GetMapping("/episodes/collections/{collectionId}/seasons")
    List<Integer> getSeasonsByCollectionId(@PathVariable("collectionId") Long collectionId);

    @GetMapping("/episodes/collections/{collectionId}/season")
    RestPageImpl<EpisodeDto> getByCollectionIdAndSeason(
            @PathVariable("collectionId") Long collectionId,
            @RequestParam("season") Integer season,
            Pageable pageable
    );

    @GetMapping("/episodes/collections/{collectionId}/season/series")
    EpisodeDto getByCollectionIdAndSeasonAndSeries(
            @PathVariable("collectionId") Long collectionId,
            @RequestParam("season") Integer season,
            @RequestParam("series") Integer series
    );

    @PostMapping("/episodes")
    EpisodeDto create(@RequestBody EpisodeRequest episodeRequest);

    @PutMapping("/episodes/{id}")
    EpisodeDto addWordsInEpisode(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file);

    @PutMapping("/episodes")
    EpisodeDto update(@RequestBody EpisodeRequest episodeRequest);

    @DeleteMapping("/episodes/{id}")
    void delete(@PathVariable("id") Long id);

}
