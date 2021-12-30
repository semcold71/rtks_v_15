package ru.samcold.rtks.services.customer;

import ru.samcold.rtks.domain.Customer;

public interface CustomerService {

    Iterable<Customer> findAll();

    Customer save(Customer customer);

    Customer findById(Integer id);

    void delete(Customer customer);

    Customer findByName(String value);
}
