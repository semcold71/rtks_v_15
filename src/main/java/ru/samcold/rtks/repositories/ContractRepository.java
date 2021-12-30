package ru.samcold.rtks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samcold.rtks.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, Integer> {
}
