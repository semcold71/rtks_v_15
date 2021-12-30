package ru.samcold.rtks.services.crane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Crane;
import ru.samcold.rtks.repositories.CraneRepository;

import java.util.Optional;

@Service
public class CraneServiceImpl implements CraneService{

    private final CraneRepository repository;

    @Autowired
    public CraneServiceImpl(CraneRepository repository) {
        this.repository = repository;
    }


    @Override
    public Iterable<Crane> findAll() {
        return repository.findAll();
    }

    @Override
    public Crane save(Crane crane) {
        return repository.save(crane);
    }

    @Override
    public Crane findById(Integer id) {
        Optional<Crane> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Crane crane) {
        repository.delete(crane);
    }
}
