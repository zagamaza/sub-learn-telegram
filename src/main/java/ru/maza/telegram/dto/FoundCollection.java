package ru.maza.telegram.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FoundCollection {

    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String poster;

}
