package ru.samcold.rtks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.samcold.rtks.domain.Work;

public interface WorkRepository extends CrudRepository<Work, Integer> {
}
