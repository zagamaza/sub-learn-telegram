package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    private Long telegramId;

    private String userName;

    private String email;

    private LocalDateTime created;

    private UserSettingDto userSettingDto;

}
