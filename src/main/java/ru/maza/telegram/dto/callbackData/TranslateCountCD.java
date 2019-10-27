package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class TranslateCountCD extends CallbackData {

    private Long usId;
    private Integer count;

    public TranslateCountCD(Long usId, Integer count) {
        super(TranslateCountCD.class.getSimpleName());
        this.usId = usId;
        this.count = count;
    }

    public TranslateCountCD() {
        super(null);
    }

}
