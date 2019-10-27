package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.ChooseStartTrialCD;

@Data
public class ChooseStartTrialButton extends Button {

    private Long episodeId;
    private Long trialId;

    public ChooseStartTrialButton(
            Long episodeId,
            Long trialId,
            String name,
            CallbackData callbackData,
            Integer countButtonInLine
    ) {
        super(name, callbackData, countButtonInLine);
        this.episodeId = episodeId;
        this.trialId = trialId;
    }

    public static ChooseStartTrialButton from(Long episodeId, Long trialId, String name, Integer count) {
        return new ChooseStartTrialButton(
                episodeId,
                trialId,
                name,
                new ChooseStartTrialCD(
                        ChooseStartTrialCD.class.getSimpleName(),
                        episodeId,
                        trialId
                ),
                count
        );
    }

}
