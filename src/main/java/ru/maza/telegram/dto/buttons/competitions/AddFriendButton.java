package ru.maza.telegram.dto.buttons.competitions;

import lombok.Data;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.AddFileCD;
import ru.maza.telegram.dto.callbackData.AddFriendCD;

@Data
public class AddFriendButton extends Button {

    public AddFriendButton(String name, Integer count) {
        super(
                name,
                new AddFriendCD(),
                count
        );
    }


}
