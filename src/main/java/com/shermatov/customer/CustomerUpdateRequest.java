package com.shermatov.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {

}
