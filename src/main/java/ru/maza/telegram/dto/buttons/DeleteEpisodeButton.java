package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.DelEpisodeCD;

@Data
public class DeleteEpisodeButton extends Button {

    private Long episodeId;

    public DeleteEpisodeButton(Long episodeId, String name, Integer count) {
        super(
                name,
                new DelEpisodeCD(episodeId),
                count
        );
        this.episodeId = episodeId;
    }


}
