package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialCondensedDto {

    private Long id;
    private Long collectionId;
    private String collectionName;
    private LocalDateTime created;

}
