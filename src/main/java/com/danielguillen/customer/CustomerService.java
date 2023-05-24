package com.danielguillen.customer;

import com.danielguillen.exception.DuplicateResourceException;
import com.danielguillen.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }
    public Customer getCustomer(Integer id){
        return customerDao.selectcustomerById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "customer with id [%s] not found".formatted(id)
                        ));

    }
    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exists
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }
        //add
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.insertCustomer(customer);

    }
}
