package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.SupportCD;

@Data
public class SupportButton extends Button {

    private Integer supportId;

    public SupportButton(Integer supportId, String name, Integer count) {
        super(
                name,
                new SupportCD(supportId),
                count
        );
        this.supportId = supportId;
    }


}
