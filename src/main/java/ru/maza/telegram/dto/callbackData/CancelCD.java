package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class CancelCD extends CallbackData {

    private String command;

    public CancelCD(String clazz, String command) {
        super(clazz);
        this.command = command;
    }

    public CancelCD() {
        super(null);
    }

}
