package ru.maza.telegram.dto.callbackData;

import lombok.Data;

@Data
public class ShowAllTrCD extends CallbackData {

    public ShowAllTrCD() {
        super(ShowAllTrCD.class.getSimpleName());
    }

}
