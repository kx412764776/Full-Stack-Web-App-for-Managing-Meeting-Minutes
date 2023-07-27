package com.apprenticeship.dto;

import com.apprenticeship.model.Member;
import org.springframework.security.core.GrantedAuthority;

import java.util.function.Function;
import java.util.stream.Collectors;

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
                        .collect(Collectors.toList())
        );
    }
}
