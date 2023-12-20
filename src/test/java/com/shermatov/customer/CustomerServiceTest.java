package com.shermatov.customer;

import com.shermatov.exception.DuplicateResourceException;
import com.shermatov.exception.RequestValidationException;
import com.shermatov.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomer(id);

        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void withThroughWhenGetCustomerReturnsEmptyOptional() {
        int id = 1;


        when(customerDao.selectCustomersById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "Customer with id [%s] not found".formatted(id)
                );


    }


    @Test
    void addCustomer() {
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex",
                email,
                21
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex",
                email,
                21
        );

        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");

        verify(customerDao, never()).insertCustomer(any());


    }



    @Test
    void deleteCustomerById() {
        int id = 10;

        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomerById(id);


    }

    @Test
    void willThrowWhenDeleteCustomerByIdNotExists() {
        int id = 10;

        when(customerDao.existsPersonWithId(id)).thenReturn(false);


        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found".formatted(id)
                );

        verify(customerDao, never()).deleteCustomerById(id);

    }


    @Test
    void canUpdateCustomerAllCustomerProperties() {

        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alex", newEmail, 23);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateCustomerName() {

        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alex", null, null);


        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerEmail() {

        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, newEmail, null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerAge() {

        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 23);

        underTest.updateCustomer(id, updateRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);


        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();


        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailAlreadyTaken() {

        int id = 1;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        String newEmail = "alex@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alex", newEmail, 23);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);


        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");


        verify(customerDao, never()).updateCustomer(any());
    }


    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {

        int id = 10;
        Customer customer = new Customer(
                id,
                "Aibek",
                "aibek@gmail.com",
                21
        );

        when(customerDao.selectCustomersById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );

        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        verify(customerDao, never()).updateCustomer(any());

    }



}
