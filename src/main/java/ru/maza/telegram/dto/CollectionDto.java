package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto {

    private Long id;

    private List<EpisodeDto> episodeDtos;

    private UserDto userDto;

    private Lang lang;

    private String name;

    private boolean isSerial;

    private LocalDateTime created;


}
