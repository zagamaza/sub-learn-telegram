package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddFriendCD extends CallbackData {

    public AddFriendCD() {
        super(AddFriendCD.class.getSimpleName());
    }

}
