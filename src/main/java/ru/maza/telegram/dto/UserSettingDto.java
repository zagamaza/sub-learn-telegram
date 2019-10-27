package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingDto implements Serializable {

    private Long id;

    private Long userId;

    private Integer wordCountInTrial;

    private Integer answerOptionsCount;

    private boolean isRemindAboutTrial;

    private Integer thresholdLearnedPercent;

    private boolean isShowAllTranslate;

}
