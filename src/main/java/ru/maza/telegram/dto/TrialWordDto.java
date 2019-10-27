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
public class TrialWordDto {

    private Long id;
    private WordDto wordDto;
    private TrialDto trialDto;
    private boolean isRight;
    private boolean isPassed;
    private boolean isLastWord;
    private LocalDateTime created;

}
