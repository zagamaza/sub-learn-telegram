package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddSearchCollectionCD;

@Data
public class AddSearchCollectionsButton extends Button {

    private Long collectionId;

    public AddSearchCollectionsButton(Long collectionId, String name, Integer count) {
        super(
                name,
                new AddSearchCollectionCD(collectionId, AddSearchCollectionCD.class.getSimpleName()),
                count
        );
        this.collectionId = collectionId;
    }


}
