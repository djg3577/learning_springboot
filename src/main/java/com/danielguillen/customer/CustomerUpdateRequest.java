package com.danielguillen.customer;

public record CustomerUpdateRequest (
    String name,
    String email,
    Integer age
){
}
