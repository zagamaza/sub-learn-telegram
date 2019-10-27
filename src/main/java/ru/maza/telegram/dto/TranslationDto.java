package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDto {

    private Long id;
    private String partSpeech;
    private List<String> translate;


}
