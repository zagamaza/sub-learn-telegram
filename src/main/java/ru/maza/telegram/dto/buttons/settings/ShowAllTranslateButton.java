package ru.maza.telegram.dto.buttons.settings;

import lombok.Data;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.ShowAllTrCD;

@Data
public class ShowAllTranslateButton extends Button {

    public ShowAllTranslateButton(String name, Integer count) {
        super(
                name,
                new ShowAllTrCD(),
                count
        );
    }


}
