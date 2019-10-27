package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.TranslateCountCD;

@Data
public class TranslateOptionsCountSettingButton extends Button {

    private Long userSettingId;
    private Integer count;

    public TranslateOptionsCountSettingButton(Long userSettingId, Integer count, String name, Integer countButton) {
        super(
                name,
                new TranslateCountCD(userSettingId, count),
                countButton
        );
        this.userSettingId = userSettingId;
        this.count = count;
    }


}
