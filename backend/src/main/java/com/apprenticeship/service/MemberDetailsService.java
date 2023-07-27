package com.apprenticeship.service;

import com.apprenticeship.repository.MemberDao;
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

    private final MemberDao memberDao;

    public MemberDetailsService(@Qualifier("memberRepository") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberDao.selectMemberByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("email %s not found", email))
                );
    }
}
