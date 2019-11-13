package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.ScheduleCD;

@Data
public class ScheduleTrialButton extends Button {

    public ScheduleTrialButton(String name, Integer count) {
        super(
                name,
                new ScheduleCD(),
                count
        );
    }


}
