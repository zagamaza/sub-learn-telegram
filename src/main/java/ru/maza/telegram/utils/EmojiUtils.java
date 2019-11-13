package ru.maza.telegram.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmojiUtils {

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
