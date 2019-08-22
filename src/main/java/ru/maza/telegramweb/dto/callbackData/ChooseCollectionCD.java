package ru.maza.telegramweb.dto.callbackData;

import lombok.Data;

@Data
public class ChooseCollectionCD extends CallbackData {

    private Long cltnId;

    public ChooseCollectionCD(String clazz, Long collectionId) {
        super(clazz);
        this.cltnId = collectionId;
    }

    public ChooseCollectionCD() {
        super(null);
    }

}
