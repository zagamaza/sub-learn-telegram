package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class CancelCD extends CallbackData {
    private boolean isCanceled;

    public CancelCD(String clazz, boolean isCanceled) {
        super(clazz);
        this.isCanceled = isCanceled;
    }
    public CancelCD() {
        super(null);
    }
}
