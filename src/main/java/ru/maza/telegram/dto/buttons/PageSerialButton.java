package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.PageSeriesCD;

@Data
public class PageSerialButton extends Button {

    private Integer season;
    private Long collectionId;
    private Integer page;
    private Boolean isLeft;

    public PageSerialButton(
            Integer season,
            Long collectionId,
            Integer page,
            Boolean isLeft,
            String name,
            Integer count
    ) {
        super(
                name,
                new PageSeriesCD(season, collectionId, page, isLeft),
                count
        );
        this.season = season;
        this.collectionId = collectionId;
        this.page = page;
        this.isLeft = isLeft;
    }


}
