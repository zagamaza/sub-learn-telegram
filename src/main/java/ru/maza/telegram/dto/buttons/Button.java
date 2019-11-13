package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CallbackData;

@Data
public class Button {

    private String name;
    private CallbackData callbackData;
    private Integer countButtonInLine;

    public Button(String name, CallbackData callbackData, Integer countButtonInLine) {
        this.name = name;
        this.callbackData = callbackData;
        this.countButtonInLine = countButtonInLine;
    }

}
