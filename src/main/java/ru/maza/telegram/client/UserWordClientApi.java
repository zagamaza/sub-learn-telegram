package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(contextId = "userWords", name = "userWords", url = "${sublearn.back.url}")
@RequestMapping("/user_words")
public interface UserWordClientApi {

    @PostMapping("/users/{userId}/reset")
    void resetProgress(@PathVariable("userId") Long userId);

}
