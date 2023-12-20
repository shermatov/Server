package com.shermatov.customer;

import com.shermatov.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest  extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.save(customer);


        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailWhenEmailNotPresent() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        var actual = underTest.existsCustomerByEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerByEmailFailWhenIdNotPresent() {
        int id = -1;

        var actual = underTest.existsCustomerById(id);

        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerByAge() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        int age = random.nextInt(16, 99);

        Customer customer = new Customer(
                name,
                email,
                age
        );


        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        var actual = underTest.existsCustomerByAge(age);

        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailWhenAgeNotPresent() {
        int age = -1;

        var actual = underTest.existsCustomerByAge(age);

        assertThat(actual).isFalse();
    }
}