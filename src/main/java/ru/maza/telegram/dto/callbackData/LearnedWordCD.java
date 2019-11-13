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

    public LearnedWordCD(Long twId, Long tlId) {
        super(LearnedWordCD.class.getSimpleName());
        this.twId = twId;
        this.tlId = tlId;
    }

    public LearnedWordCD() {
        super(null);
    }

}
