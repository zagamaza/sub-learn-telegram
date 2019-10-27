package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.ChooseTrialCD;

@Data
public class ChooseTrialButton extends Button {

    private Long trialId;

    public ChooseTrialButton(
            String name,
            Integer countButtonInLine,
            Long trialId
    ) {
        super(name, new ChooseTrialCD(trialId), countButtonInLine);
        this.trialId = trialId;
    }

}
