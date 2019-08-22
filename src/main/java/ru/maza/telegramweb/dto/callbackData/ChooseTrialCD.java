package ru.maza.telegramweb.dto.callbackData;

import lombok.Data;

@Data
public class ChooseTrialCD extends CallbackData {
    private Long trialId;
    private boolean isStart;

    public ChooseTrialCD(String clazz, Long trialId, boolean isStart) {
        super(clazz);
        this.trialId = trialId;
        this.isStart = isStart;
    }
    public ChooseTrialCD() {
        super(null);
    }
}
