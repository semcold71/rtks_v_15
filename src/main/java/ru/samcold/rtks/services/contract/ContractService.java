package ru.samcold.rtks.services.contract;

import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.domain.Crane;

import java.util.Optional;

public interface ContractService {

    Iterable<Contract> findAll();

    Contract save(Contract contract);

    Contract findById(Integer id);

    void delete(Contract contract);
}
