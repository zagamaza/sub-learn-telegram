package ru.maza.telegramweb.dto.callbackData;

import lombok.Data;

@Data
public class ChooseTranslateCD extends CallbackData {
    private Long wId;
    private Long tId;
    private Long rwId;

    public ChooseTranslateCD(String className, Long wId, Long tId, Long rwId) {
        super(className);
        this.wId = wId;
        this.tId = tId;
        this.rwId = rwId;
    }
    public ChooseTranslateCD() {
        super(null);
    }
}
