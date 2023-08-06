package com.apprenticeship.requestsAndResponses;

import com.apprenticeship.dto.MemberDTO;

public record LoginResponse(MemberDTO memberDTO, String token) {
}
