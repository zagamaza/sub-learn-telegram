package ru.maza.telegram.dto;

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
    private Lang lang;
    private String name;
    private String url;
    private Integer rating;
    private boolean isShared;
    private Boolean isSerial;
    private LocalDateTime created;

}
