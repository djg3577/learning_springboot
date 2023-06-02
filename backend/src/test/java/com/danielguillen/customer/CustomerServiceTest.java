package com.danielguillen.customer;

import com.danielguillen.exception.DuplicateResourceException;
import com.danielguillen.exception.RequestValidationException;
import com.danielguillen.exception.ResourceNotFoundException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

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
        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDao).selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));
        //When
        Customer actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(customer);

    }
    @Test
    void willThrowWhenGetCustomerEmptyOptional() {
        //Given
        int id = 10;
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.empty());
        //When

        //Then
        assertThatThrownBy(() ->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                19
        );
        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo("Alex");
        assertThat(capturedCustomer.getEmail()).isEqualTo(email);
        assertThat(capturedCustomer.getAge()).isEqualTo(19);
    }
    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        //Given
        String email = "alex@gmail.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "Alex",
                email,
                19
        );
        //When
       assertThatThrownBy(() -> underTest.addCustomer(request))
               .isInstanceOf(DuplicateResourceException.class)
               .hasMessage("email already taken");

        //Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        //When
        underTest.deleteCustomerById(id);

        //Then
        verify(customerDao).deleteCustomerById(id);

    }
    @Test
    void willThrowdDeleteCustomerByIdNotExists() {
        //Given
        int id = 10;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessage("customer with id [%s] not found".formatted(id));

        //Then
        verify(customerDao, never()).deleteCustomerById(id);

    }

    @Test
    void canUpdateAllCustomerProperties() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Alexander@gmail,com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Alexander", newEmail,19);
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexander", null,null);
        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void canUpdateOnlyCustomerEmail() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        String newEmail = "Alexander@gmail,com";
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail,null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);


        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void canUpdateOnlyCustomerAge() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null,200);

        //When
        underTest.updateCustomer(id, updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }
    @Test
    void willThrowWhenEmailTakenDuringUpdate() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        String newEmail = "Alexander@gmail,com";
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail,null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);


        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        //Then

        verify(customerDao, never()).updateCustomer(any());

    }
    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        //Given

        int id = 10;
        Customer customer = new Customer(
                id,
                "Alex",
                "Alex@gmail.com",
                19
        );
        when(customerDao.selectcustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge()
        );
        //When
        assertThatThrownBy(() -> underTest.updateCustomer(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        //Then
        verify(customerDao,never()).updateCustomer(any());
    }

}
