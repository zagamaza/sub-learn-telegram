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
public class CollectionRequest {

    private Long id;

    private Lang lang;

    private Long userId;

    private String name;

    private String url;

    private Integer rating;

    private boolean isShared;

    private LocalDateTime created;

    private boolean isSerial;

}
