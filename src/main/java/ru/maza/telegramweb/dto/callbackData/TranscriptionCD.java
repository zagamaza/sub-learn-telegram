package ru.maza.telegramweb.dto.callbackData;

import lombok.Data;
import lombok.NoArgsConstructor;

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
