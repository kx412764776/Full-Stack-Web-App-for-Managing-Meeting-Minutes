package com.apprenticeship.dto;

import com.apprenticeship.model.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class is used to map a Member object to a MemberDTO object.
 */
@Service
public class MemberDTOMapper implements Function<Member, MemberDTO> {

    @Override
    public MemberDTO apply(Member member) {
        return new MemberDTO(
                member.getMemberId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmail(),
                member.getPassword(),
                member.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                member.getUsername()
        );
    }
}
