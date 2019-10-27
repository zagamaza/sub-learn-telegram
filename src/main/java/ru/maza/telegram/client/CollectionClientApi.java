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
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;

import java.util.List;

@FeignClient(contextId = "collections", name = "collections", url = "${sublearn.back.url}")
public interface CollectionClientApi {

    @GetMapping("/collections/{id}")
    CollectionDto get(@PathVariable("id") Long id);

    @GetMapping("/collections/condensed/users/{userId}")
    List<CollectionCondensedDto> getCollectionByUserId(@PathVariable("userId") Long userId, Pageable pageable);

    @GetMapping("/collections/condensed/users/{userId}/count")
    Integer getCountCollectionByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/collections")
    List<CollectionCondensedDto> search(@RequestParam("collectionName") String collectionName, Pageable pageable);

    @GetMapping("/collections/{id}/users/{userId}/copy")
    CollectionDto copy(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

    @PutMapping("/collections/{id}")
    CollectionDto updateIsSerial(@PathVariable("id") Long id, @RequestParam("isSerial") Boolean isSerial);

    @PutMapping("/collections")
    CollectionDto update(@RequestBody CollectionRequest collectionRequest);

    @PostMapping("/collections")
    CollectionDto create(@RequestBody CollectionRequest collectionRequest);

    @DeleteMapping("/collections/{id}")
    void delete(@PathVariable("id") Long id);

    @DeleteMapping("/collections/{id}/users/{userId}")
    void deleteLinkUserToCollection(@PathVariable("id") Long id, @PathVariable("userId") Long userId);

}
