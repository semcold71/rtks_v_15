package ru.samcold.rtks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samcold.rtks.domain.Statement;

public interface StatementRepository extends JpaRepository<Statement, Integer> {
}
