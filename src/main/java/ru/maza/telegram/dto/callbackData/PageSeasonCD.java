package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class PageSeasonCD extends CallbackData {

    private Long clnId;
    private Boolean isL;
    private Integer pg;

    public PageSeasonCD(Long clnId, Boolean isL, Integer pg) {
        super(PageSeasonCD.class.getSimpleName());
        this.clnId = clnId;
        this.pg = pg;
        this.isL = isL;
    }

    public PageSeasonCD() {
        super(null);
    }

}
