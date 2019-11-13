package ru.maza.telegram.infra.service;

import ru.maza.telegram.infra.dao.redis.entity.Command;

import java.util.List;

public interface CommandInfraService {

    Command get(Long id);

    List<Command> getAll();

    Command save(Command command);

    void remove(Long id);

}
