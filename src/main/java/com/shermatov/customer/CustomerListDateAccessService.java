package com.shermatov.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDateAccessService implements CustomerDao {
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21

        );
        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                19

        );
        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomersById(Integer id) {
        return customers.stream().filter(customer -> customer.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public boolean existsPersonWithAge(Integer age) {
        return customers.stream()
                .anyMatch(c -> c.getAge().equals(age));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }

}
