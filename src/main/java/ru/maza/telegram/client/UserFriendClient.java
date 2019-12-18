package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.competition.UserFriendDto;

@FeignClient(contextId = "userFriend", name = "userFriend", url = "${sublearn.competition.url}")
public interface UserFriendClient {

    @GetMapping("/user_friends/{id}")
    UserFriendDto get(@PathVariable("id") Integer id);

    @PostMapping("/user_friends")
    UserFriendDto create(@RequestBody UserFriendDto userFriendDto);

    @PutMapping("/user_friends")
    UserFriendDto update(@RequestBody UserFriendDto userFriendDto);

    @DeleteMapping("/user_friends/{id}")
    void delete(@PathVariable("id") Integer id);

}
