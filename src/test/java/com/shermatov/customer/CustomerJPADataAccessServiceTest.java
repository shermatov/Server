package com.shermatov.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomersById() {
        int id = 1;

        underTest.selectCustomersById(id);

        verify(customerRepository).findById(id);

    }

    @Test
    void insertCustomer() {
        Customer customer = new Customer (
                1, "Ali", "ali@gmail.com", 2
        );

        underTest.insertCustomer(customer);

        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        String email = "ali@gmail.com";

        underTest.existsPersonWithEmail(email);

        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        int id = 1;

        underTest.existsPersonWithId(id);

        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void existsPersonWithAge() {
        int age = 1;

        underTest.existsPersonWithAge(age);

        verify(customerRepository).existsCustomerByAge(age);
    }

    @Test
    void deleteCustomerById() {
       int id = 1;

        underTest.deleteCustomerById(id);

        verify(customerRepository).deleteById(id);

    }

    @Test
    void updateCustomer() {
        Customer update = new Customer (
                1, "Ali", "ali@gmail.com", 2
        );

        underTest.updateCustomer(update);

        verify(customerRepository).save(update);
    }
}