package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.client.model.RestPageImpl;
import ru.maza.telegram.dto.UserActionDto;

@FeignClient(contextId = "userActions", name = "userActions", url = "${sublearn.back.url}")
public interface UserActionClient {

    @GetMapping("/user_actions/{id}")
    UserActionDto get(@PathVariable("id") Long id);

    @GetMapping("/user_actions")
    RestPageImpl<UserActionDto> getAll(Pageable pageable);

    @GetMapping("/user_actions/users/{userId}")
    RestPageImpl<UserActionDto> getByUserName(@PathVariable("userId") Long userId, Pageable pageable);

    @PostMapping("/user_actions")
    void create(@RequestBody UserActionDto userActionDto);

    @DeleteMapping("/user_actions/{id}")
    void delete(@PathVariable("id") Long id);

}

