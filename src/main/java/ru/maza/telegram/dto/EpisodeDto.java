package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDto implements Serializable {

    private Long id;

    private List<WordDto> words;

    private CollectionDto collectionDto;

    private Integer season;

    private Integer episode;

    private Integer learnedPercent;

}
