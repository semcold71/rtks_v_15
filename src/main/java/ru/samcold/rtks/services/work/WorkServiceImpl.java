package ru.samcold.rtks.services.work;

import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Work;
import ru.samcold.rtks.repositories.WorkRepository;

import java.util.Optional;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository repository;

    public WorkServiceImpl(WorkRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Work> findAll() {
        return repository.findAll();
    }

    @Override
    public Work save(Work work) {
        return repository.save(work);
    }

    @Override
    public Work findById(Integer id) {
        Optional<Work> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Work work) {
        repository.delete(work);
    }
}
