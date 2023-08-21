package com.apprenticeship.service;

import com.apprenticeship.model.Member;
import com.apprenticeship.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberDetailsServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberDetailsService memberDetailsService;

    @Test
    void loadUserByUsername_withValidUsername_shouldReturnUserDetails() {
        // given
        String email = "test@example.com";
        Member member = new Member(
                1,
                "Connor",
                "Chen",
                email,
                "password",
                "APPRENTICE"
        );

        when(memberRepository.findMemberByEmail(email))
                .thenReturn(Optional.of(member));

        // when
        UserDetails result = memberDetailsService.loadUserByUsername(email);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getAuthorities()).isNotEmpty();
        assertThat(result.getAuthorities().size()).isEqualTo(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority())
                .isEqualTo(member.getMemberRole());
    }

    @Test
    void loadUserByUsername_withInvalidUsername_shouldThrowException() {
        // given
        String invalidEmail = "invalid@email.com";

        when(memberRepository.findMemberByEmail(invalidEmail))
                .thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> memberDetailsService.loadUserByUsername(invalidEmail))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("email %s not found".formatted(invalidEmail));
    }

}