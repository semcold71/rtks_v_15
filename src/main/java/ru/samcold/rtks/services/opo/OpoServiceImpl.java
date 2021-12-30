package ru.samcold.rtks.services.opo;

import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Opo;
import ru.samcold.rtks.repositories.OpoRepository;

import java.util.Optional;

@Service
public class OpoServiceImpl implements OpoService {

    private final OpoRepository repository;

    public OpoServiceImpl(OpoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Opo> findAll() {
        return repository.findAll();
    }

    @Override
    public Opo save(Opo opo) {
        return repository.save(opo);
    }

    @Override
    public Opo findById(Integer id) {
        Optional<Opo> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Opo opo) {
        repository.delete(opo);
    }
}
