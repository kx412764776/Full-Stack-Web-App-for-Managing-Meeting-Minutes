package com.apprenticeship.service;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.dto.MemberDTOMapper;
import com.apprenticeship.jwt.JWTUtil;
import com.apprenticeship.model.Member;
import com.apprenticeship.requestsAndResponses.LoginRequest;
import com.apprenticeship.requestsAndResponses.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * This class is used to handle the login process.
 */
@Service
public class LoginService {


    private final AuthenticationManager authenticationManager;

    private final MemberDTOMapper memberDTOMapper;

    private final JWTUtil jwtUtil;


    public LoginService(AuthenticationManager authenticationManager,
                        MemberDTOMapper memberDTOMapper,
                        JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.memberDTOMapper = memberDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method is used to judge whether the username and password are correct.
     * And if they are correct, it will return a token.
     * @param request
     * @return
     */
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        Object principal = authentication.getPrincipal();
        MemberDTO memberDTO = memberDTOMapper.apply((Member) principal);
        String token = jwtUtil.issueToken(memberDTO.username(), memberDTO.memberRoles());
        return new LoginResponse(memberDTO, token);
    }




}
