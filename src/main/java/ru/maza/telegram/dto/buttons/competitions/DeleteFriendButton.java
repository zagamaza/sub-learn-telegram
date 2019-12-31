package ru.maza.telegram.dto.buttons.competitions;

import lombok.Data;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.DeleteFriendCD;

@Data
public class DeleteFriendButton extends Button {

    public DeleteFriendButton(String name, Integer count) {
        super(
                name,
                new DeleteFriendCD(),
                count
        );
    }


}
