package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class SupportCD extends CallbackData {

    private Integer supportId;

    public SupportCD(Integer supportId) {
        super(SupportCD.class.getSimpleName());
        this.supportId = supportId;
    }

    public SupportCD() {
        super(null);
    }

}
