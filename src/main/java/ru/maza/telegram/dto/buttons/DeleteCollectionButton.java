package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddFileCD;
import ru.maza.telegram.dto.callbackData.DelCollectionCD;

@Data
public class DeleteCollectionButton extends Button {

    private Long collectionId;

    public DeleteCollectionButton(Long collectionId, String name, Integer count) {
        super(
                name,
                new DelCollectionCD(collectionId),
                count
        );
        this.collectionId = collectionId;
    }


}
