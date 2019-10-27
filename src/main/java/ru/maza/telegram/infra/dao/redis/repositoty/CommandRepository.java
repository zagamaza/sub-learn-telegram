package ru.maza.telegram.infra.dao.redis.repositoty;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.maza.telegram.infra.dao.redis.entity.Command;

@Repository
public interface CommandRepository extends CrudRepository<Command, Long> {

}
