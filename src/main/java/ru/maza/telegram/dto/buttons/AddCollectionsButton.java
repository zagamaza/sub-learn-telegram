package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddCollectionCD;

@Data
public class AddCollectionsButton extends Button {

    public AddCollectionsButton(String name, Integer count) {
        super(
                name,
                new AddCollectionCD(AddCollectionCD.class.getSimpleName()),
                count
        );
    }


}
