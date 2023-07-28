package com.apprenticeship.service;

import com.apprenticeship.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is used to load user-specific data.
 */
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(@Qualifier("memberJPARepository") MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("email %s not found", email))
                );
    }
}
