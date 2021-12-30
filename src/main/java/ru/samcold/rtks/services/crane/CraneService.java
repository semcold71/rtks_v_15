package ru.samcold.rtks.services.crane;

import ru.samcold.rtks.domain.Crane;

public interface CraneService {

    Iterable<Crane> findAll();

    Crane save(Crane crane);

    Crane findById(Integer id);

    void delete(Crane crane);
}
