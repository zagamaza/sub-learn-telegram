package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ChooseCollectionCD extends CallbackData {

    private Long cltnId;
    private Boolean isSerial;

    public ChooseCollectionCD(Long collectionId, Boolean isSerial) {
        super(ChooseCollectionCD.class.getSimpleName());
        this.isSerial = isSerial;
        this.cltnId = collectionId;
    }

    public ChooseCollectionCD() {
        super(null);
    }

}
