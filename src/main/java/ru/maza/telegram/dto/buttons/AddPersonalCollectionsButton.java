package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddPersonalCollectionCD;

@Data
public class AddPersonalCollectionsButton extends Button {

    public AddPersonalCollectionsButton(String name, Integer count) {
        super(
                name,
                new AddPersonalCollectionCD(AddPersonalCollectionCD.class.getSimpleName()),
                count
        );
    }


}
