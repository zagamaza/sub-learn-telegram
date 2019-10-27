package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class AddFileCD extends CallbackData {

    private Long episodeId;

    public AddFileCD(Long episodeId) {
        super(AddFileCD.class.getSimpleName());
        this.episodeId = episodeId;
    }

    public AddFileCD() {
        super(null);
    }

}
