package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class AddCollectionCD extends CallbackData {

    public AddCollectionCD(String clazz) {
        super(clazz);
    }

    public AddCollectionCD() {
        super(null);
    }

}
