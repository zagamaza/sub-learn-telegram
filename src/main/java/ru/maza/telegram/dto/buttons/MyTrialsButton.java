package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.MyTrialsCD;

@Data
public class MyTrialsButton extends Button {

    public MyTrialsButton(String name) {
        super(
                name,
                new MyTrialsCD(),
                1
        );
    }


}
