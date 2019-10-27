package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ScheduleCD extends CallbackData {

    public ScheduleCD() {
        super(ScheduleCD.class.getSimpleName());
    }

}
