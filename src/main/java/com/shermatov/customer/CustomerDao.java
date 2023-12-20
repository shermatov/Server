package com.shermatov.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomersById(Integer id);

    void insertCustomer(Customer customer);

    boolean existsPersonWithEmail(String email);

    boolean existsPersonWithId(Integer id);

    boolean existsPersonWithAge(Integer age);

    void deleteCustomerById(Integer customerId);

    void updateCustomer(Customer update);




}