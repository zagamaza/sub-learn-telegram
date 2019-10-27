package ru.maza.telegram.dto.buttons;

import lombok.Data;
import ru.maza.telegram.dto.callbackData.CallbackData;
import ru.maza.telegram.dto.callbackData.ChooseIsSerialCD;

@Data
public class ChooseIsSerialButton extends Button {

    private Long collectionId;

    public ChooseIsSerialButton(
            Long collectionId,
            String name,
            CallbackData callbackData,
            Integer countButtonInLine
    ) {
        super(name, callbackData, countButtonInLine);
        this.collectionId = collectionId;
    }

    public static ChooseIsSerialButton from(Long collectionId, boolean isSerial) {
        return new ChooseIsSerialButton(
                collectionId,
                isSerial ? "Serial"
                        : "Film",
                new ChooseIsSerialCD(
                        ChooseIsSerialCD.class.getSimpleName(),
                        collectionId,
                        isSerial
                ),
                2
        );
    }

}
