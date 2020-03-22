package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LearnedWordCountCD extends CallbackData {

    private Integer learnedCount;

    public LearnedWordCountCD(Integer learnedCount) {
        super(LearnedWordCountCD.class.getSimpleName());
        this.learnedCount = learnedCount;
    }

    public LearnedWordCountCD() {
        super(null);
    }



}
