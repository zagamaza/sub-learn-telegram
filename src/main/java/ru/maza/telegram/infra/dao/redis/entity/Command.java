package ru.maza.telegram.infra.dao.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("command")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command implements Serializable {

    @Id
    private Long userId;
    private String textCommands;
    private Long commandId;

}
