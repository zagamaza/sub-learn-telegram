package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActionDto {

    private Long id;

    private String data;

    private Long userId;

    private ActionType actionType;

    private LocalDateTime created;

    public UserActionDto(String data, Long userId, ActionType actionType) {
        this.data = data;
        this.userId = userId;
        this.actionType = actionType;
    }

}
