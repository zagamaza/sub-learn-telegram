package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationDto implements Serializable {

    private Long id;
    private String partSpeech;
    private List<String> translate;


}
