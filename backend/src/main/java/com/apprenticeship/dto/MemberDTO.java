package com.apprenticeship.dto;

import java.util.List;

public record MemberDTO(
        Integer memberId,
        String firstName,
        String lastName,
        String email,
        List<String> memberRoles,
        String username
) {
}
