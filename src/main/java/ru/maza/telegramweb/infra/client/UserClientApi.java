package ru.maza.telegramweb.infra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegramweb.dto.UserDto;


@FeignClient(contextId = "users", name = "users", url = "localhost:8082/api")
public interface UserClientApi {

    @GetMapping("/users/{id}")
    UserDto get(@PathVariable("id") Long id);

    @PostMapping("/users")
    UserDto save(@RequestBody UserDto userDto);

}
