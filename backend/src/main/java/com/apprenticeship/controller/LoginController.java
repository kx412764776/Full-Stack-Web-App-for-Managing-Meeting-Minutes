package com.apprenticeship.controller;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.jwt.JWTUtil;
import com.apprenticeship.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is responsible for handling requests related to login and registration.
 */
@RestController
@RequestMapping("/apprenticeship")
public class LoginController {

    private final MemberService memberService;
    private final LoginService loginService;
    private final JWTUtil jwtUtil;

    public LoginController(MemberService memberService,
                           LoginService loginService,
                           JWTUtil jwtUtil) {
        this.memberService = memberService;
        this.loginService = loginService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, response.token())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRegistrationRequest request) {
        memberService.registerMember(request);
        String jwtToken = jwtUtil.issueToken(request.email(), request.memberRole());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();
    }

    @PostMapping("/memberInfo/{memberEmail}")
    public ResponseEntity<?> getMemberByEmail(
            @PathVariable("memberEmail") String email) {
        MemberDTO memberDTO = memberService.getMemberByEmail(email);
        if (memberDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberDTO);
    }
}
