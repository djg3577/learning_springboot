package com.danielguillen.customer;

import jakarta.persistence.criteria.CriteriaBuilder;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age
){


}
