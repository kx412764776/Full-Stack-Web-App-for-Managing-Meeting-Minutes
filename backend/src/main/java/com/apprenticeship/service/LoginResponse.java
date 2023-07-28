package com.apprenticeship.service;

import com.apprenticeship.dto.MemberDTO;

public record LoginResponse(MemberDTO memberDTO, String token) {
}
