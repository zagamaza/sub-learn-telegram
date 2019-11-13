package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialRequest {

    private Long id;

    private String name;

    private Long episodeId;

    private Long userId;

    private LocalDateTime created;

    public static TrialRequest get(Long episodeId, Long userId) {
        return new TrialRequest(
                null,
                "sd",
                episodeId,
                userId,
                LocalDateTime.now()
        );
    }

}
