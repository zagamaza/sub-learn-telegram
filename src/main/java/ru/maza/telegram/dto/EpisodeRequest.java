package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EpisodeRequest {

    private Long id;

    private List<WordDto> words;

    private Long collectionId;

    private Integer season;

    private Integer episode;

}
