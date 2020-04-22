package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
public class CCD extends CallbackData { // ChooseTranslateButton

    private Long tw; //trialWordId
    private Long t; //trialId
    private Long w; //wordId
    private Long r; //rightWordId

    public CCD(Long tw, Long t, Long w, Long r) {
        super(CCD.class.getSimpleName());
        this.tw = tw;
        this.t = t;
        this.w = w;
        this.r = r;
    }

    public CCD() {
        super(null);
    }

}
