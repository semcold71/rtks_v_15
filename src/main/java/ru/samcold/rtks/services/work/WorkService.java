package ru.samcold.rtks.services.work;

import ru.samcold.rtks.domain.Work;

public interface WorkService {

    Iterable<Work> findAll();

    Work save(Work work);

    Work findById(Integer id);

    void delete(Work work);
}
