package ru.maza.telegram.dto.buttons.settings;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.maza.telegram.dto.buttons.Button;
import ru.maza.telegram.dto.callbackData.LearnedWordCountCD;

@Data
public class LearnedWordCountButton extends Button {

    private Integer learnedWordCount;

    public LearnedWordCountButton(String name, Integer count, Integer learnedWordCount) {
        super(name, new LearnedWordCountCD(learnedWordCount), count);
        this.learnedWordCount = learnedWordCount;
    }

    public LearnedWordCountButton(){
        super(null,null, null);
    }

}
