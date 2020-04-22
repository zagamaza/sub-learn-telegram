package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maza.telegram.dto.callbackData.CCD;

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

    public static TrialWordRequest from(CCD chooseTranslateCD) {
        return new TrialWordRequest(
                chooseTranslateCD.getTw(),
                null,
                null,
                chooseTranslateCD.getR().equals(chooseTranslateCD.getW()),
                true
        );
    }

}
