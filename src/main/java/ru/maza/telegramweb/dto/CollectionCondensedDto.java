package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCondensedDto {

    private Long id;
    private String lang;
    private String name;
    private LocalDateTime created;

    public static CollectionCondensedDto from(CollectionDto collectionDto) {
        return new CollectionCondensedDto(
                collectionDto.getId(),
                collectionDto.getLang(),
                collectionDto.getName(),
                collectionDto.getCreated()
        );
    }
}
