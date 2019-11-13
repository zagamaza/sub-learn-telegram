package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.MyCollectionsCD;

@Data
public class MyCollectionsButton extends Button {

    public MyCollectionsButton(String name) {
        super(
                name,
                new MyCollectionsCD(MyCollectionsCD.class.getSimpleName()),
                1
        );
    }


}
