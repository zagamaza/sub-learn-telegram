package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class AddPersonalCollectionCD extends CallbackData {

    public AddPersonalCollectionCD(String clazz) {
        super(clazz);
    }

    public AddPersonalCollectionCD() {
        super(null);
    }

}
