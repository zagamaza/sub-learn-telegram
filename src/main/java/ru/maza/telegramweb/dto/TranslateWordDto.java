package ru.maza.telegramweb.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslateWordDto {

    private Integer wordId;
    private Long collectId;
    private Integer trslteId;
    private List<Integer> trslnsId;

}
