package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class MyTrialsCD extends CallbackData {

    public MyTrialsCD() {
        super(MyTrialsCD.class.getSimpleName());
    }

}
