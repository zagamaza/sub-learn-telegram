package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ChooseTrialCD extends CallbackData {

    private Long trialId;

    public ChooseTrialCD(Long trialId) {
        super(ChooseTrialCD.class.getSimpleName());
        this.trialId = trialId;
    }

    public ChooseTrialCD() {
        super(null);
    }

}
