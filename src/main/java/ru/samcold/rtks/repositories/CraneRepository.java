package ru.samcold.rtks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samcold.rtks.domain.Crane;

public interface CraneRepository extends JpaRepository<Crane, Integer> {
}
