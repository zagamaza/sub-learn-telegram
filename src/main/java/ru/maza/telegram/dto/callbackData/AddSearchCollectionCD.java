package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddSearchCollectionCD extends CallbackData {

    private Long clctnId;
    private String imdbId;

    public AddSearchCollectionCD(Long clctnId, String imdbId, String clazz) {
        super(clazz);
        this.clctnId = clctnId;
        this.imdbId = imdbId;
    }

    public AddSearchCollectionCD() {
        super(null);
    }

}
