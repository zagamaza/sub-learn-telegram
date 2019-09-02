package ru.maza.telegram.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.maza.telegram.dto.CollectionCondensedDto;
import ru.maza.telegram.dto.CollectionDto;
import ru.maza.telegram.dto.CollectionRequest;

import javax.validation.Valid;
import java.util.List;

@FeignClient(contextId = "collections", name = "collections", url = "localhost:8082/api")
public interface CollectionClientApi {

    @GetMapping("/{id}")
    CollectionDto get(@PathVariable Long id);

    @GetMapping("/condensed/users/{userId}")
    List<CollectionCondensedDto> getCollectionByUserId(@PathVariable Long userId);

    @PutMapping
    CollectionDto update(@RequestBody CollectionRequest collectionRequest);

    @PostMapping
    CollectionDto create(@RequestBody CollectionRequest collectionRequest);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);


}
