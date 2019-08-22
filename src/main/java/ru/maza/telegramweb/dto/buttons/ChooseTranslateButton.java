package ru.maza.telegramweb.dto.buttons;

import lombok.Getter;
import lombok.Setter;
import ru.maza.telegramweb.dto.callbackData.CallbackData;

@Getter
@Setter
public class ChooseTranslateButton extends Button {
    private Long wordId;
    private Long trialId;
    private Boolean isRight;

    public ChooseTranslateButton(String name, CallbackData callbackData, Long wordId, Long trialId, Boolean isRight, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
        this.wordId = wordId;
        this.trialId = trialId;
        this.isRight = isRight;
    }

}
