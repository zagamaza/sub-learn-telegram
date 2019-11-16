package ru.maza.telegram.dto;

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

    private EpisodeDto episodeDto;

    private List<TrialWordDto> trialWords;

    private UserDto userDto;

    private LocalDateTime created;

}
