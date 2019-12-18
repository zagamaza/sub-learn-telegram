package ru.maza.telegram.dto.competition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionUserDto {

    private Long id;
    private OffsetDateTime created;
    private Integer experience;
    private Long levelId;
    private Long telegramId;
    private String userName;

}
