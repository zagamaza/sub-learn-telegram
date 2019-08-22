package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {

    private Long id;
    private List<WordDto> words;
    private String lang;
    private String name;
    private LocalDateTime created;


}
