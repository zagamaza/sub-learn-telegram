package ru.maza.telegram.utils;

import com.vdurmont.emoji.EmojiManager;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmojiUtils {

    public static final String NOT = EmojiManager.getForAlias("x").getUnicode();
    public static final String OK = EmojiManager.getForAlias("white_check_mark").getUnicode();
    public static final String RIGHT = EmojiManager.getForAlias("ballot_box_with_check").getUnicode();

    public String extractEmojiPercent(Integer percent) {
        if (percent == null) {
            percent = 0;
        }
        StringBuilder presentInEmoji = new StringBuilder();
        String number = String.valueOf(percent);
        for (int i = 0; i < number.length(); i++) {
            int j = Character.digit(number.charAt(i), 10);
            presentInEmoji.append(j);
            presentInEmoji.append('\u20E3');
        }
        return presentInEmoji.toString();
    }

}
