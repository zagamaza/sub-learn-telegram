package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maza.telegram.dto.callbackData.CTlteCD;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrialWordRequest {

    private Long id;
    private Long wordId;
    private Long trialId;
    boolean isRight;
    boolean isPassed;

    public static TrialWordRequest from(CTlteCD chooseTranslateCD) {
        return new TrialWordRequest(
                chooseTranslateCD.getTwId(),
                null,
                null,
                chooseTranslateCD.getRwid().equals(chooseTranslateCD.getWdId()),
                true
        );
    }

}
