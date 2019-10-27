package ru.maza.telegram.dto.buttons;

import lombok.Data;

@Data
public class SearchCollectionButton extends Button {

    public SearchCollectionButton(String name, Integer count) {
        super(name, null, count);
    }


}
