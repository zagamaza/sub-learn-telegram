package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.MySettingsCD;

@Data
public class MySettingsButton extends Button {

    public MySettingsButton(String name) {
        super(
                name,
                new MySettingsCD(MySettingsCD.class.getSimpleName()),
                1
        );
    }


}
