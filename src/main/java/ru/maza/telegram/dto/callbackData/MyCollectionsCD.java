package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class MyCollectionsCD extends CallbackData {

    public MyCollectionsCD(String clazz) {
        super(clazz);
    }

    public MyCollectionsCD() {
        super(null);
    }

}
