package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddPersonalCollectionCD;

@Data
public class AddPersonalCollectionsButton extends Button {

    private Long collectionId;
    private Long episodeId;

    public AddPersonalCollectionsButton(Long collectionId, Long episodeId, String name, Integer count) {
        super(
                name,
                new AddPersonalCollectionCD(collectionId, episodeId, AddPersonalCollectionCD.class.getSimpleName()),
                count
        );
        this.collectionId = collectionId;
        this.episodeId = episodeId;
    }


}
