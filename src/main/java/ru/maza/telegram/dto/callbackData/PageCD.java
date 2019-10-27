package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class PageCD extends CallbackData {

    private String entity;
    private Integer page;
    private Boolean isLeft;

    public PageCD(String entity, Integer page, Boolean isLeft) {
        super(PageCD.class.getSimpleName());
        this.entity = entity;
        this.page = page;
        this.isLeft = isLeft;
    }

    public PageCD() {
        super(null);
    }

}
