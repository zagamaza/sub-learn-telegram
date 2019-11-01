package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChsSeriesCD extends CallbackData {

    private Long clnId;
    private Long epdId;

    public ChsSeriesCD(Long clnId, Long epdId) {
        super(ChsSeriesCD.class.getSimpleName());
        this.clnId = clnId;
        this.epdId = epdId;
    }

    public ChsSeriesCD() {
        super(null);
    }

}
