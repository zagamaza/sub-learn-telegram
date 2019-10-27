package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.WordCountCD;

@Data
public class WordCountSettingButton extends Button {

    private Long userSettingId;
    private Integer count;

    public WordCountSettingButton(Long userSettingId, Integer count, String name, Integer countButton) {
        super(
                name,
                new WordCountCD(userSettingId, count),
                countButton
        );
        this.userSettingId = userSettingId;
        this.count = count;
    }


}
