package ru.maza.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maza.telegram.dto.callbackData.ChooseCollectionCD;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialRequestDto {

    private Long id;

    private String name;

    private Long episodeId;

    private LocalDateTime created;

    public static TrialRequestDto from(ChooseCollectionCD chooseCollectionCD, Integer userId) {
        return new TrialRequestDto(
                null,
                "sd",
                userId.longValue(),
                chooseCollectionCD.getCltnId(),
                LocalDateTime.now()
        );
    }

}
