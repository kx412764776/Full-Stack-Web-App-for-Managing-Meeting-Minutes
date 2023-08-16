package com.apprenticeship.dto;

import com.apprenticeship.enums.MemberRole;

public record SignatureStatusDTO(
        String name,
        String email,
        String memberRole,
        Integer signatureStatus
) {
}
