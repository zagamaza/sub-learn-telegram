package ru.maza.telegramweb.dto.buttons;

import ru.maza.telegramweb.dto.callbackData.CallbackData;

public class TranscriptionButton extends Button {
    private Long wordId;

    public TranscriptionButton(String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
    }
}
