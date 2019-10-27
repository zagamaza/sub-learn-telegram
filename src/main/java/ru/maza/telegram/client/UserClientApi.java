package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.UserDto;
import ru.maza.telegram.dto.UserRequest;


@FeignClient(contextId = "users", name = "users", url = "${sublearn.back.url}")
public interface UserClientApi {

    @GetMapping("/users/{id}")
    UserDto get(@PathVariable("id") Long id);

    @GetMapping("/users/telegram/{id}")
    UserDto getByTelegramId(@PathVariable("id") Long id);

    @PostMapping("/users")
    UserDto save(@RequestBody UserRequest userRequest);

    @PutMapping("/users")
    UserDto update(@RequestBody UserRequest userRequest);

}
