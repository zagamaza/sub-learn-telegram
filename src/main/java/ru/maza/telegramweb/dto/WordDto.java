package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordDto {

    private Long id;
    private String word;
    private String transcription;
    private List<String> translation;
    private String lang;
    private LocalDateTime created;

}
