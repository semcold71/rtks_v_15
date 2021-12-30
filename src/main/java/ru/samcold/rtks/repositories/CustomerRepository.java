package ru.samcold.rtks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samcold.rtks.domain.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByName(String name);
}
