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
public class ResultDto {

    private Long id;
    private WordDto wordDto;
    private TrialDto trialDto;
    boolean isRight;
    private LocalDateTime created;

}
