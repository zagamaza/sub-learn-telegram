package ru.maza.telegram.dto.buttons;

import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.CancelCD;

public class CancelButton extends Button {

    public CancelButton(String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
    }

    public CancelButton(String name, String command, Integer countButtonInLine) {
        super(name, new CancelCD(CancelCD.class.getSimpleName(), command), countButtonInLine);
    }

}
