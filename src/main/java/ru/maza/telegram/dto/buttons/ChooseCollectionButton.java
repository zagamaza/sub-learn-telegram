package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.ChooseCollectionCD;

@Data
public class ChooseCollectionButton extends Button {

    private Long collectionId;
    private Boolean isSerial;

    public ChooseCollectionButton(
            Long collectionId,
            Boolean isSerial,
            String name,
            Integer countButtonInLine
    ) {
        super(name, new ChooseCollectionCD(collectionId, isSerial), countButtonInLine);
        this.collectionId = collectionId;
        this.isSerial = isSerial;
    }

}
