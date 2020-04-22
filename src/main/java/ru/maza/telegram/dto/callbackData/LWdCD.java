package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Data
public class LWdCD extends CallbackData { //ChooseLearnedWordButton

    private Long tw; //trialWordId
    private Long tl; //
    private Long wd; //trialId
    private Long isK; //isKnow

    public LWdCD(Long tw, Long tl, Long wd, Long isK) {
        super(LWdCD.class.getSimpleName());
        this.tw = tw;
        this.tl = tl;
        this.wd = wd;
        this.isK = isK;
    }

    public LWdCD() {
        super(null);
    }

}
