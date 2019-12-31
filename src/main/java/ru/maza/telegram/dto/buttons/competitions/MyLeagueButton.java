package ru.maza.telegram.dto.buttons.competitions;

import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.MyCompetitionsCD;
import ru.maza.telegram.dto.callbackData.MyLeagueCD;

public class MyLeagueButton extends Button {

    public MyLeagueButton(String name) {
        super(
                name,
                new MyLeagueCD(),
                1
        );
    }

}
