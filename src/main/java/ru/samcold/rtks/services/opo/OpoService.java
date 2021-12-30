package ru.samcold.rtks.services.opo;

import ru.samcold.rtks.domain.Opo;

public interface OpoService {

    Iterable<Opo> findAll();

    Opo save(Opo opo);

    Opo findById(Integer id);

    void delete(Opo opo);
}
