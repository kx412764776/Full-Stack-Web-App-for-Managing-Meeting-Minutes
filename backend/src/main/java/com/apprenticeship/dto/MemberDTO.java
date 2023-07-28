package com.apprenticeship.dto;

import java.util.List;

public record MemberDTO(
        Integer memberId,
        String firstName,
        String lastName,
        String email,
        String password,
        List<String> memberRoles,
        String username
) {
}
