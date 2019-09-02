package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zagamaza.sublearn.infra.dao.entity.EpisodeEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDto {

    private Long id;

    List<WordDto> words;

    private CollectionDto collectionDto;

    private Integer season;

    private Integer episode;


}
