package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class MySettingsCD extends CallbackData {

    public MySettingsCD(String clazz) {
        super(clazz);
    }

    public MySettingsCD() {
        super(null);
    }

}
