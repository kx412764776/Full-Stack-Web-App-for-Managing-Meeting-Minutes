package com.apprenticeship.service;

public record MemberRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String memberRole
) {
}
