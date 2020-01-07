package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.LeardWdCD;

@Data
public class ChooseLearnedWordButton extends Button {

    private Long trialWordId;
    private Long trialId;
    private Long wordId;
    private Long isKnow;

    public ChooseLearnedWordButton(
            String name,
            Long trialWordId,
            Long trialId,
            Long wordId,
            Long isKnow,
            Integer countButtonInLine
    ) {
        super(name, new LeardWdCD(trialWordId, trialId, wordId, isKnow), countButtonInLine);
        this.trialWordId = trialWordId;
        this.trialId = trialId;
        this.wordId = wordId;
        this.isKnow = isKnow;
    }

}
