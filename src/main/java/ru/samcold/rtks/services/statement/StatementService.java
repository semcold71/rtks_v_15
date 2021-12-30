package ru.samcold.rtks.services.statement;

import ru.samcold.rtks.domain.Statement;

public interface StatementService {

    Iterable<Statement> findAll();

    Statement save(Statement statement);

    Statement findById(Integer id);

    void delete(Statement statement);
}
