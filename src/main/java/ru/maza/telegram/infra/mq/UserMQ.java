package ru.maza.telegram.infra.mq;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMQ implements Serializable {

    private Long id;

    private Long telegramId;

    private String userName;

}


