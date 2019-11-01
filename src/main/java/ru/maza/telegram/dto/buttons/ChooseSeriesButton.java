package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.ChsSeriesCD;
import ru.maza.telegram.dto.callbackData.LearnedWordCD;

@Data
public class ChooseSeriesButton extends Button {

    private Long collectionId;
    private Long episodeId;

    public ChooseSeriesButton(
            Long collectionId,
            Long episodeId,
            String name,
            Integer countButtonInLine
    ) {
        super(name, new ChsSeriesCD(collectionId, episodeId), countButtonInLine);
        this.collectionId = collectionId;
        this.episodeId = episodeId;
    }

}
