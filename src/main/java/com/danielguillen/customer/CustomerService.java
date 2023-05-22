package com.danielguillen.customer;

import com.danielguillen.exception.ResourceNotFound;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }
    public Customer getCustomer(Integer id){
        return customerDao.selectcustomerById(id)
                .orElseThrow(
                        () -> new ResourceNotFound(
                                "customer with id [%s] not found".formatted(id)
                        ));

    }
}
