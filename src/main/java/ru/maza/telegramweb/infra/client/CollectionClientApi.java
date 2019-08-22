package ru.maza.telegramweb.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegramweb.dto.CollectionCondensedDto;
import ru.maza.telegramweb.dto.CollectionDto;

import java.util.List;

@FeignClient(contextId = "collections", name = "collections", url = "localhost:8082/api")
public interface CollectionClientApi {


    @GetMapping("/collections/{id}")
    CollectionDto getCollection(@PathVariable("id") Long id);

    @GetMapping("/collections/condensed/users/{userId}")
    List<CollectionCondensedDto> getCollectionByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/collections")
    CollectionDto save(@RequestBody CollectionDto collectionDto);


}
