package com.apprenticeship.requestsAndResponses;

public record MemberRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String memberRole
) {
}
