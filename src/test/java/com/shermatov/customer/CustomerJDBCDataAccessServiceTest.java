package com.shermatov.customer;

import com.shermatov.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private Random random = new Random();
    private CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        //Given
        Customer customer = new Customer(
                        FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        // When

        List<Customer> actual = underTest.selectAllCustomers();

        // After
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomersById() {

        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);
        int id = underTest.selectAllCustomers().stream().filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional <Customer> actual = underTest.selectCustomersById(id);

        // Then

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
        
        



    }

    @Test
    void willReturnEmptyWhenSelectCustomersById() {

        //Given
        int id = -1;

        // When
        var actual = underTest.selectCustomersById(id);

        // Then
        assertThat(actual).isEmpty();


    }

    @Test
    void insertCustomer() {
        // Given

    }

    @Test
    void existsCustomerWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        boolean actual = underTest.existsPersonWithEmail(email);
        assertThat(actual).isTrue();
    }

    void existsCustomerWithEmailReturnsFalseWhenDoesNotExists() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        boolean actual = underTest.existsPersonWithEmail(email);
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        boolean actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        int id = -1;

        var actual = underTest.existsPersonWithId(id);

        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithAge() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );
        int customerAge = customer.getAge();

        underTest.insertCustomer(customer);

        boolean actual = underTest.existsPersonWithAge(customerAge);

        assertThat(actual).isTrue();
    }

    @Test
    void deleteCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTest.deleteCustomerById(id);

        Optional<Customer> actual = underTest.selectCustomersById(id);

        assertThat(actual).isNotPresent();

    }

    @Test
    void updateCustomerName() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "test";

        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomersById(id);

        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(newName);
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomersById(id);

        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(newEmail);
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomersById(id);

        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(newAge);
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        var newAge = 100;
        var newName = FAKER.name().fullName();

        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        update.setAge(age);
        update.setName(newName);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomersById(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        Customer update = new Customer();
        update.setId(id);


        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomersById(id);


        assertThat(actual).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer1.getId()).isEqualTo(id);
            assertThat(customer1.getName()).isEqualTo(customer.getName());
            assertThat(customer1.getEmail()).isEqualTo(customer.getEmail());
            assertThat(customer1.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomer() {


    }
}