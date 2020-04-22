package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CCD;

@Data
public class ChooseTranslateButton extends Button {

    private Long trialWordId;
    private Long trialId;
    private Long wordId;
    private Long rightWordId;

    public ChooseTranslateButton(
            String name,
            Long trialWordId,
            Long trialId,
            Long wordId,
            Long rightWordId,
            Integer countButtonInLine
    ) {
        super(name, new CCD(trialWordId, trialId, wordId, rightWordId), countButtonInLine);
        this.trialWordId = trialWordId;
        this.trialId = trialId;
        this.wordId = wordId;
        this.rightWordId = rightWordId;
    }

}
