package ru.maza.telegram.dto.buttons;

import ru.maza.telegram.dto.callbackData.CallbackData;

public class CancelButton extends Button {
    public CancelButton(String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
    }
}
