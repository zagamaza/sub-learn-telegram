package ru.maza.telegram.dto.buttons.settings;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.ResetProgressCD;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResetProgressButton extends Button {

    public ResetProgressButton(String name, Integer count) {
        super(name, new ResetProgressCD(), count);
    }

    public ResetProgressButton() {
        super(null, null, null);
    }

}
