package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.PageCD;

@Data
public class PageButton extends Button {

    private String entity;
    private Integer page;
    private Boolean isLeft;

    public PageButton(String entity, Integer page, Boolean isLeft, String name, Integer count) {
        super(
                name,
                new PageCD(entity, page, isLeft),
                count
        );
        this.entity = entity;
        this.page = page;
        this.isLeft = isLeft;
    }


}
