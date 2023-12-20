package com.shermatov.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age

) {
}
