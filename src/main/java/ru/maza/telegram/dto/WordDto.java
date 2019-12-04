package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDto implements Serializable {

    private Long id;
    private String word;
    private String transcription;
    private String mainTranslation;
    private List<TranslationDto> translation;
    private String lang;
    private LocalDateTime created;

}
