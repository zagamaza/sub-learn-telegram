package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class WordCountCD extends CallbackData {

    private Long usId;
    private Integer count;

    public WordCountCD(Long usId, Integer count) {
        super(WordCountCD.class.getSimpleName());
        this.usId = usId;
        this.count = count;
    }

    public WordCountCD() {
        super(null);
    }

}
