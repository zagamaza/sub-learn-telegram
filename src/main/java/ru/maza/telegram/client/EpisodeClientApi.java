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
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.EpisodeRequest;

import java.util.List;

@FeignClient(contextId = "episodes", name = "episodes", url = "${sublearn.back.url}")
public interface EpisodeClientApi {

    @GetMapping("/episodes/{id}")
    EpisodeDto get(@PathVariable("id") Long id);

    @GetMapping("/episodes/collections/{collectionId}")
    List<EpisodeDto> getAllByCollectionId(@PathVariable("collectionId") Long collectionId, Pageable pageable);

    @GetMapping("/episodes/collections/{collectionId}/count")
    Integer getCountByCollectionId(@PathVariable("collectionId") Long collectionId);

    @GetMapping("/episodes/{id}/users/{userId}")
    Integer getLearnedPercent(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @PostMapping("/episodes")
    EpisodeDto create(@RequestBody EpisodeRequest episodeRequest);

    @PutMapping("/episodes/{id}")
    EpisodeDto addWordsInEpisode(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file);

    @PutMapping("/episodes")
    EpisodeDto update(@RequestBody EpisodeRequest episodeRequest);

    @DeleteMapping("/episodes/{id}")
    void delete(@PathVariable("id") Long id);

}
