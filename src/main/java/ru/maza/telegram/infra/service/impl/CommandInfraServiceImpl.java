package ru.maza.telegram.infra.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.maza.telegram.infra.dao.redis.entity.Command;
import ru.maza.telegram.infra.dao.redis.repositoty.CommandRepository;
import ru.maza.telegram.infra.service.CommandInfraService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandInfraServiceImpl implements CommandInfraService {

    private final CommandRepository repository;

    @Override
    public Command get(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Command> getAll() {
        return (List<Command>)repository.findAll();
    }

    @Override
    public Command save(Command command) {
        return repository.save(command);
    }

    @Override
    public void remove(Long id) {
        repository.deleteById(id);
    }

}
