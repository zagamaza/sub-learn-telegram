package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class TranscriptionCD extends CallbackData {
    private Long wordId;

    public TranscriptionCD(String clazz, Long wordId) {
        super(clazz);
        this.wordId = wordId;
    }
    public TranscriptionCD() {
        super(null);
    }
}
