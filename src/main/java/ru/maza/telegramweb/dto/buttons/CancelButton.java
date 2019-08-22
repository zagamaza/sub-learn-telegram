package ru.maza.telegramweb.dto.buttons;

import ru.maza.telegramweb.dto.callbackData.CallbackData;

public class CancelButton extends Button {
    public CancelButton(String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
    }
}
