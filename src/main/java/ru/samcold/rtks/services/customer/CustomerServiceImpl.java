package ru.samcold.rtks.services.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.samcold.rtks.domain.Customer;
import ru.samcold.rtks.repositories.CustomerRepository;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public Customer findById(Integer id) {
        Optional<Customer> optional = repository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public void delete(Customer customer) {
        repository.delete(customer);
    }

    @Override
    public Customer findByName(String value) {
        Optional<Customer> optionalCustomer = repository.findByName(value);
        return optionalCustomer.orElse(null);
    }
}
