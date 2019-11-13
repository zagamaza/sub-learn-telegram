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
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDto implements Serializable {

    private Long id;

    private List<EpisodeDto> episodeDtos;

    private UserDto userDto;

    private Lang lang;

    private String name;

    private String url;

    private Integer rating;

    private boolean isShared;

    private boolean isSerial;

    private LocalDateTime created;


}
