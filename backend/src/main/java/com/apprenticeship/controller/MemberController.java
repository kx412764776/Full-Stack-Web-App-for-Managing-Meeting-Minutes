package com.apprenticeship.controller;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apprenticeship/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Get member info by email
    @PostMapping("/memberInfo/{memberEmail}")
    public ResponseEntity<?> getMemberByEmail(
            @PathVariable("memberEmail") String email) {
        MemberDTO memberDTO = memberService.getMemberByEmail(email);
        if (memberDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberDTO);
    }

    // Get all member info
    @PostMapping("/memberInfo")
    public ResponseEntity<?> getAllMemberInfo() {
        return ResponseEntity.ok(memberService.getAllMemberInfo());
    }

    // check if member is existed by email
    // if existed, immediately return the member info
    @PostMapping("/checkMember/{memberEmailPrefix}")
    public ResponseEntity<?> checkMemberByEmail(
            @PathVariable("memberEmailPrefix") String emailPrefix) {
        List<String> memberEmailAndName = memberService.checkMembersByEmailPrefix(emailPrefix);
        if (memberEmailAndName == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(memberEmailAndName);
    }

}
