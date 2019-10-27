package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ChooseIsSerialCD extends CallbackData {

    private Long cltnId;

    private Boolean isSerial;

    public ChooseIsSerialCD(String clazz, Long collectionId, boolean isSerial) {
        super(clazz);
        this.cltnId = collectionId;
        this.isSerial = isSerial;
    }

    public ChooseIsSerialCD() {
        super(null);
    }

}
