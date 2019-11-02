package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CTlteCD extends CallbackData {

    private Long tw;
    private Long tl;
    private Long wd;
    private Long rw;

    public CTlteCD(Long tw, Long tl, Long wd, Long rw) {
        super(CTlteCD.class.getSimpleName());
        this.tw = tw;
        this.tl = tl;
        this.wd = wd;
        this.rw = rw;
    }

    public CTlteCD() {
        super(null);
    }

}
