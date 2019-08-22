package ru.maza.telegramweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.maza.telegramweb.dto.callbackData.ChooseCollectionCD;
import ru.maza.telegramweb.dto.callbackData.ChooseTrialCD;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrialRequestDto {

    private Long id;

    private String name;

    private Long userId;

    private Long collectionId;

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
