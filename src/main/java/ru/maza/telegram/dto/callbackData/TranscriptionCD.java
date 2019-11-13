package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class TranscriptionCD extends CallbackData {

    private Long wordId;

    public TranscriptionCD(Long wordId) {
        super(TranscriptionCD.class.getSimpleName());
        this.wordId = wordId;
    }

    public TranscriptionCD() {
        super(null);
    }

}
