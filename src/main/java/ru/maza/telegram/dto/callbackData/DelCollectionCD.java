package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class DelCollectionCD extends CallbackData {

    private Long collectionId;

    public DelCollectionCD(Long collectionId) {
        super(DelCollectionCD.class.getSimpleName());
        this.collectionId = collectionId;
    }

    public DelCollectionCD() {
        super(null);
    }

}
