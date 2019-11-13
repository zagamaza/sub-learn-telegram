package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class AddPersonalCollectionCD extends CallbackData {

    private Long ctnId;
    private Long epdId;

    public AddPersonalCollectionCD(Long ctnId, Long epdId, String clazz) {
        super(clazz);
        this.ctnId = ctnId;
        this.epdId = epdId;
    }

    public AddPersonalCollectionCD() {
        super(null);
    }

}
