package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class PageSeriesCD extends CallbackData {

    private Integer sn;
    private Long clnId;
    private Integer pg;
    private Boolean isL;

    public PageSeriesCD(Integer sn, Long clnId, Integer pg, Boolean isL) {
        super(PageSeriesCD.class.getSimpleName());
        this.sn = sn;
        this.clnId = clnId;
        this.pg = pg;
        this.isL = isL;
    }

    public PageSeriesCD() {
        super(null);
    }

}
