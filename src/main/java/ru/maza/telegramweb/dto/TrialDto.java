package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialDto {

    private Long id;

    private String name;

    private UserDto userDto;

    private CollectionDto collectionDto;

    private List<ResultDto> results;

    private Integer percent;

    private Integer correctPercent;

    private LocalDateTime created;

}
