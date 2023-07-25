package com.apprenticeship.controller.member;

import com.apprenticeship.model.Member;
import com.apprenticeship.service.MemberService.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping()
    public List<Member> getUsers() {
        return memberService.selectAllUsers();
    }
}
