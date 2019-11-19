package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LearnedWordCD extends CallbackData {

    private Long twId;
    private Long tlId;
    private Long wdId;

    public LearnedWordCD(Long twId, Long tlId, Long wdId) {
        super(LearnedWordCD.class.getSimpleName());
        this.twId = twId;
        this.tlId = tlId;
        this.wdId = wdId;
    }

    public LearnedWordCD() {
        super(null);
    }

}
