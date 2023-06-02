package com.danielguillen.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

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
        //When
        underTest.selectAllCustomers();

        //Then
        verify(customerRepository)
        .findAll();

    }

    @Test
    void selectcustomerById() {
        //Given
        int id = 1;
        //When
        underTest.selectcustomerById(id);

        //Then
        verify(customerRepository).findById(id);

    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer = new Customer(
                1,"daniel","daniel@gmail.com",19
        );
        //When
        underTest.insertCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = "foo@gmail.com";
        //When
        underTest.existsPersonWithEmail(email);

        //Then
        verify(customerRepository).existsCustomerByEmail(email);

    }

    @Test
    void existsPersonWithId() {
        //Given
        int id =1;
        //When
        underTest.existsPersonWithId(id);

        //Then
        verify(customerRepository).existsCustomerById(id);

    }

    @Test
    void deleteCustomerById() {
        //Given
        int id =1;
        //When
        underTest.deleteCustomerById(id);

        //Then
        verify(customerRepository).deleteById(id);

    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = new Customer(
                1,"Ali-A","alia@gmail.com",2
        );
        //When
        underTest.updateCustomer(customer);

        //Then
        verify(customerRepository).save(customer);

    }
}