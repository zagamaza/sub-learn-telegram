package ru.maza.telegram.dto.buttons.competitions;

import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.MyCompetitionsCD;

public class MyCompetitionsButton extends Button {

    public MyCompetitionsButton(String name) {
        super(
                name,
                new MyCompetitionsCD(),
                1
        );
    }

}
