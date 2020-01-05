package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.PageSeasonCD;

@Data
public class PageSeasonButton extends Button {

    private Long collectionId;
    private Integer page;
    private Boolean isLeft;

    public PageSeasonButton(
            Long collectionId,
            Integer page,
            Boolean isLeft,
            String name,
            Integer count
    ) {
        super(
                name,
                new PageSeasonCD(collectionId, isLeft, page),
                count
        );
        this.collectionId = collectionId;
        this.page = page;
        this.isLeft = isLeft;
    }


}
