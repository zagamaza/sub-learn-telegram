package ru.maza.telegram.dto;

import lombok.Data;

import java.util.List;

@Data
public class EpisodeDto {

    private Long id;

    private List<WordDto> words;

    private CollectionDto collectionDto;

    private Integer season;

    private Integer episode;

    private Integer learnedPercent;

}
