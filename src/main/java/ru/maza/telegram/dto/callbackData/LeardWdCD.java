package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LeardWdCD extends CallbackData {

    private Long twId;
    private Long tlId;
    private Long wdId;
    private Long isK;

    public LeardWdCD(Long twId, Long tlId, Long wdId, Long isK) {
        super(LeardWdCD.class.getSimpleName());
        this.twId = twId;
        this.tlId = tlId;
        this.wdId = wdId;
        this.isK = isK;
    }

    public LeardWdCD() {
        super(null);
    }

}
