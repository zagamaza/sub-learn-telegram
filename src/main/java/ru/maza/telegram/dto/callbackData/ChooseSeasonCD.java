package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ChooseSeasonCD extends CallbackData {

    private Long clnId;
    private Integer season;

    public ChooseSeasonCD(Long clnId, Integer season) {
        super(ChooseSeasonCD.class.getSimpleName());
        this.clnId = clnId;
        this.season = season;
    }

    public ChooseSeasonCD() {
        super(null);
    }

}
