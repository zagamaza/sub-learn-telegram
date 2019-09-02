package ru.maza.telegram.dto.buttons;

import ru.maza.telegram.dto.callbackData.CallbackData;

public class TranscriptionButton extends Button {
    private Long wordId;

    public TranscriptionButton(String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
    }
}
