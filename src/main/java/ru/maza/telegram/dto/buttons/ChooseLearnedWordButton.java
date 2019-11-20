package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CTlteCD;
import ru.maza.telegram.dto.callbackData.LearnedWordCD;

@Data
public class ChooseLearnedWordButton extends Button {

    private Long trialWordId;
    private Long trialId;
    private Long wordId;

    public ChooseLearnedWordButton(
            String name,
            Long trialWordId,
            Long trialId,
            Long wordId,
            Integer countButtonInLine
    ) {
        super(name, new LearnedWordCD(trialWordId, trialId, wordId), countButtonInLine);
        this.trialWordId = trialWordId;
        this.trialId = trialId;
        this.wordId = wordId;
    }

}
