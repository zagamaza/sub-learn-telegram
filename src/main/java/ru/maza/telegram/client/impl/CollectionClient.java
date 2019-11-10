package ru.maza.telegram.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.maza.telegram.client.CollectionClientApi;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionClient {

    private final CollectionClientApi collectionClientApi;

    @Cacheable(value = "collections", key = "#id")
    public CollectionDto get(Long id) {
        return collectionClientApi.get(id);
    }

    @Caching(cacheable = {
            @Cacheable("collections"),
            @Cacheable(value = "user", key = "#userId")
    })
    public RestPageImpl<CollectionCondensedDto> getCollectionByUserId(Long userId, Pageable pageable) {
        return collectionClientApi.getCollectionByUserId(userId, pageable);
    }

    public List<CollectionCondensedDto> search(String collectionName, Pageable pageable) {
        return collectionClientApi.search(collectionName, pageable);
    }

    public CollectionDto copy(Long id, Long userId) {
        return collectionClientApi.copy(id, userId);
    }

    public CollectionDto updateIsSerial(Long id, Boolean isSerial) {
        return collectionClientApi.updateIsSerial(id, isSerial);
    }

    @Caching(evict = {
            @CacheEvict("collections"),
            @CacheEvict(value = "user", key = "#collectionRequest.userId")
    },
             put = @CachePut(value = "episodes", key = "#collectionRequest.id"))
    public CollectionDto update(CollectionRequest collectionRequest) {
        return collectionClientApi.update(collectionRequest);
    }

    @Caching(evict = {
            @CacheEvict("collections"),
            @CacheEvict(value = "user", key = "#collectionRequest.userId")
    })
    public CollectionDto create(CollectionRequest collectionRequest) {
        return collectionClientApi.create(collectionRequest);
    }

    @CacheEvict(value = "collections", key = "#id")
    public void delete(Long id) {
        collectionClientApi.delete(id);
    }

    @Caching(evict = {
            @CacheEvict("collections"),
            @CacheEvict(value = "user", key = "#userId")
    })
    public void deleteLinkUserToCollection(Long id, Long userId) {
        collectionClientApi.deleteLinkUserToCollection(id, userId);
    }

}
