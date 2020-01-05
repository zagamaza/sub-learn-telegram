package ru.maza.telegram.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.maza.telegram.client.EpisodeClientApi;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.EpisodeDto;
import ru.maza.telegram.dto.EpisodeRequest;

@Service
@RequiredArgsConstructor
public class EpisodeClient {

    private final EpisodeClientApi episodeClientApi;

    @Cacheable(value = "episodes", key = "#id")
    public EpisodeDto get(Long id) {
        return episodeClientApi.get(id);
    }

    @Caching(cacheable = {
            @Cacheable("episodes"),
            @Cacheable(value = "collection", key = "#collectionId")
    })
    public RestPageImpl<EpisodeDto> getAllByCollectionId(Long collectionId, Pageable pageable) {
        return episodeClientApi.getAllByCollectionId(collectionId, pageable);
    }

    public Integer getLearnedPercent(Long id, Long userId) {
        return episodeClientApi.getLearnedPercent(id, userId);
    }

    public RestPageImpl<Integer> getSeasonsByCollectionId(Long collectionId, Pageable pageable) {
        return episodeClientApi.getSeasonsByCollectionId(collectionId, pageable);
    }

    public RestPageImpl<EpisodeDto> getByCollectionIdAndSeason(Long collectionId, Integer season, Pageable pageable) {
        return episodeClientApi.getByCollectionIdAndSeason(collectionId, season, pageable);
    }

    public EpisodeDto getByCollectionIdAndSeasonAndSeries(Long collectionId, Integer season, Integer series) {
        return episodeClientApi.getByCollectionIdAndSeasonAndSeries(collectionId, season, series);
    }

    @Caching(evict = {
            @CacheEvict("episodes"),
            @CacheEvict(value = "collection", key = "#episodeRequest.collectionId")
    })
    public EpisodeDto create(EpisodeRequest episodeRequest) {
        return episodeClientApi.create(episodeRequest);
    }

    public EpisodeDto addWordsInEpisode(Long id, MultipartFile file) {
        return episodeClientApi.addWordsInEpisode(id, file);
    }

    @Caching(evict = {
            @CacheEvict("episodes"),
            @CacheEvict(value = "collection", key = "#episodeRequest.collectionId")
    },
             put = @CachePut(value = "episodes", key = "#episodeRequest.id"))
    public EpisodeDto update(EpisodeRequest episodeRequest) {
        return episodeClientApi.update(episodeRequest);
    }

    @CacheEvict(value = "episodes", key = "#id")
    public void delete(Long id) {
        episodeClientApi.delete(id);
    }


}
