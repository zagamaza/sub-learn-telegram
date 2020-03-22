package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResetProgressCD extends CallbackData {

    public ResetProgressCD() {
        super(ResetProgressCD.class.getSimpleName());
    }

}
