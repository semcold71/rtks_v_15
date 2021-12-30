package ru.samcold.rtks.services.contract;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Contract;
import ru.samcold.rtks.repositories.ContractRepository;

import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;

    public ContractServiceImpl(ContractRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Contract> findAll() {
        return repository.findAll();
    }

    @Override
    public Contract save(Contract contract) {
        return repository.save(contract);
    }

    @Override
    public Contract findById(Integer id) {
        Optional<Contract> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Contract contract) {
        repository.delete(contract);
    }
}
