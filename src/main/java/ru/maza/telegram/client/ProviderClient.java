package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.maza.telegram.dto.FoundCollection;

import java.util.List;

@FeignClient(contextId = "provider", name = "provider", url = "${sublearn.provider.url}")
public interface ProviderClient {

    @GetMapping("/collections/find")
    List<FoundCollection> findCollections(@RequestParam("title") String title);

    @PostMapping("/collections/{imdbId}/subtitles/upload")
    void upload(@PathVariable("imdbId") String imdbId, @RequestParam("userId") Long userId);

}