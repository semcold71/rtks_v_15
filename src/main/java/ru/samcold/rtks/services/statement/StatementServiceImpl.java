package ru.samcold.rtks.services.statement;

import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Statement;
import ru.samcold.rtks.repositories.StatementRepository;

import java.util.Optional;

@Service
public class StatementServiceImpl implements StatementService {

    private final StatementRepository repository;

    public StatementServiceImpl(StatementRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Statement> findAll() {
        return repository.findAll();
    }

    @Override
    public Statement save(Statement statement) {
        return repository.save(statement);
    }

    @Override
    public Statement findById(Integer id) {
        Optional<Statement> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Statement statement) {
        repository.delete(statement);
    }
}
