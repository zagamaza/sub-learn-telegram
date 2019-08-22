package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maza.telegramweb.dto.callbackData.ChooseTranslateCD;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultRequestDto {

    private Long id;
    private Long wordId;
    private Long trialId;
    boolean isRight;
    private LocalDateTime created;

    public static ResultRequestDto from(ChooseTranslateCD chooseTranslateCD) {
        return new ResultRequestDto(
                null,
                chooseTranslateCD.getRwId(),
                chooseTranslateCD.getTId(),
                chooseTranslateCD.getRwId().equals(chooseTranslateCD.getWId()),
                LocalDateTime.now()
        );
    }
}
