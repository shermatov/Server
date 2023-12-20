package com.shermatov.customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsCustomerByEmail(String email);

    boolean existsCustomerById(Integer integer);

    boolean existsCustomerByAge(Integer age);

}
