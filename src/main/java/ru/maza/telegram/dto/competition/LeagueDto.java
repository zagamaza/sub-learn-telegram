package ru.maza.telegram.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueDto {

    private Long id;
    private Long userId;
    private String userName;
    private Integer experience;
    private Long levelId;
    private OffsetDateTime created;

}
