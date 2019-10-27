package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ChooseStartTrialCD extends CallbackData {

    private Long episodeId;
    private Long trialId;

    public ChooseStartTrialCD(String clazz, Long episodeId, Long trialId) {
        super(clazz);
        this.episodeId = episodeId;
        this.trialId = trialId;
    }

    public ChooseStartTrialCD() {
        super(null);
    }

}
