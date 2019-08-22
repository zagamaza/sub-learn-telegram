package ru.maza.telegramweb.dto.buttons;

import lombok.Data;
import ru.maza.telegramweb.dto.callbackData.CallbackData;

@Data
public class ChooseTrialButton extends Button {

    private Long trialId;
    private boolean isStartTrial;

    public ChooseTrialButton(String name, CallbackData callbackData, Integer countButtonInLine, Long trialId, boolean isStartTrial) {
        super(name, callbackData, countButtonInLine);
        this.trialId = trialId;
        this.isStartTrial = isStartTrial;
    }
}
