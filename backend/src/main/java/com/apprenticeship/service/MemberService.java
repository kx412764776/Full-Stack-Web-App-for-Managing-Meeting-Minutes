package com.apprenticeship.service;

import com.apprenticeship.repository.MemberDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberDao memberDao;

    private final PasswordEncoder passwordEncoder;

    public MemberService(
            @Qualifier("memberRepository") MemberDao memberDao,
            PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    // TODO: addMember()


}
