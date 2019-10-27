package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class AddSearchCollectionCD extends CallbackData {

    private Long clctnId;

    public AddSearchCollectionCD(Long clctnId, String clazz) {
        super(clazz);
        this.clctnId = clctnId;
    }

    public AddSearchCollectionCD() {
        super(null);
    }

}
