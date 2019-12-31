package ru.maza.telegram.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFriendDto {

    private Long id;
    private Long userId;
    private Long userFriendId;
    private OffsetDateTime created;

    public UserFriendDto(Long userId, Long userFriendId) {
        this.userId = userId;
        this.userFriendId = userFriendId;
    }

}
