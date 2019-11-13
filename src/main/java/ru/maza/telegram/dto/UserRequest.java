package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;

    private Long telegramId;

    private String userName;

    private String email;

    private LocalDateTime created;


    public static UserRequest from(User user) {
        return new UserRequest(
                null,
                user.getId().longValue(),
                user.getUserName(),
                null,
                LocalDateTime.now()
        );
    }

}
