package ru.maza.telegram.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.maza.telegram.dto.UserSettingDto;

import javax.validation.Valid;

@FeignClient(contextId = "userSetting", name = "userSetting", url = "${sublearn.back.url}")
public interface UserSettingClientApi {

    @GetMapping("/user_settings/{id}")
    UserSettingDto get(@PathVariable("id") Long id);

    @GetMapping("/user_settings/users/{userId}")
    UserSettingDto getByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/user_settings")
    UserSettingDto create(@Valid @RequestBody UserSettingDto userSettingDto);

    @PutMapping("/user_settings")
    UserSettingDto update(@RequestBody UserSettingDto userSettingDto);

    @DeleteMapping("/user_settings/{id}")
    void delete(@PathVariable("id") Long id);

}
