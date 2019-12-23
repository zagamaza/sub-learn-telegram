package ru.maza.telegram.dto.callbackData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteFriendCD extends CallbackData {

    public DeleteFriendCD() {
        super(DeleteFriendCD.class.getSimpleName());
    }

}
