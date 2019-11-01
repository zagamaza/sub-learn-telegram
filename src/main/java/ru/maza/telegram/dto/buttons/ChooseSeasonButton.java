package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.ChooseSeasonCD;

@Data
public class ChooseSeasonButton extends Button {

    private Long collectionId;
    private Integer season;

    public ChooseSeasonButton(
            String name,
            Long collectionId,
            Integer season,
            Integer countButtonInLine
    ) {
        super(name, new ChooseSeasonCD(collectionId, season), countButtonInLine);
        this.collectionId = collectionId;
        this.season = season;
    }

}
