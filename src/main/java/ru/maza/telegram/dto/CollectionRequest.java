package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zagamaza.sublearn.infra.dao.entity.Lang;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest {

    private Long id;

    @NotNull
    private Lang lang;

    @NotNull
    private Long userId;

    @NotNull
    private String name;

    private LocalDateTime created;

    private boolean isSerial;

}
