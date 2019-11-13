package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.AddFileCD;

@Data
public class AddFileButton extends Button {

    private Long episodeId;

    public AddFileButton(Long episodeId, String name, Integer count) {
        super(
                name,
                new AddFileCD(episodeId),
                count
        );
        this.episodeId = episodeId;
    }


}
