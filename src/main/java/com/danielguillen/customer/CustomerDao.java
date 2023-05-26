package com.danielguillen.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectcustomerById(Integer id);
    void insertCustomer(Customer customer);
    Optional<Customer> existsPersonWithEmail(String email);
    boolean existsPersonWithId(Integer customerId);
    void deleteCustomerById(Integer customerId);
    void updateCustomer(Customer update);
}
