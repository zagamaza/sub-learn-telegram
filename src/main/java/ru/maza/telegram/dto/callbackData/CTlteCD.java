package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CTlteCD extends CallbackData {

    private Long twId;
    private Long tlId;
    private Long wdId;
    private Long rwid;

    public CTlteCD(Long twId, Long tlId, Long wdId, Long rwid) {
        super(CTlteCD.class.getSimpleName());
        this.twId = twId;
        this.tlId = tlId;
        this.wdId = wdId;
        this.rwid = rwid;
    }

    public CTlteCD() {
        super(null);
    }

}
