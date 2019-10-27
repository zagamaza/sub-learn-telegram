package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.TranscriptionCD;

@Data
public class TranscriptionButton extends Button {

    private Long wordId;

    public TranscriptionButton(String name, Long wordId, Integer countButtonInLine) {
        super(name, new TranscriptionCD(wordId), countButtonInLine);
        this.wordId = wordId;
    }

}
