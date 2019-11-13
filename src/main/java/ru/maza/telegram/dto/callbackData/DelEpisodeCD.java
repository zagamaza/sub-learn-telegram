package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class DelEpisodeCD extends CallbackData {

    private Long episodeId;

    public DelEpisodeCD(Long episodeId) {
        super(DelEpisodeCD.class.getSimpleName());
        this.episodeId = episodeId;
    }

    public DelEpisodeCD() {
        super(null);
    }

}
