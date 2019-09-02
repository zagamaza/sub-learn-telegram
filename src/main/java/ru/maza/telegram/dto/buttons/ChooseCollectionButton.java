package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CallbackData;

@Data
public class ChooseCollectionButton extends Button {
    private Long collectionId;

    public ChooseCollectionButton(Long collectionId, String name, CallbackData callbackData, Integer countButtonInLine) {
        super(name, callbackData, countButtonInLine);
        this.collectionId = collectionId;
    }

}
