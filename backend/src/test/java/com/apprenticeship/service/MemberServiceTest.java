package com.apprenticeship.service;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.dto.MemberDTOMapper;
import com.apprenticeship.exception.DuplicateResourceException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.Member;
import com.apprenticeship.repository.MemberRepository;
import com.apprenticeship.requestsAndResponses.MemberRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberDTOMapper memberDTOMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    MemberService memberService;

    @Test
    void registerMember_withNewEmail_shouldSaveToDb() {
        // given
        MemberRegistrationRequest request = new MemberRegistrationRequest(
                "Connor",
                "Chen",
                "test@example.com",
                "password",
                "APPRENTICE"
        );

        when(memberRepository.existsMemberByEmail(anyString())).thenReturn(false);

        String encodedPassword = "dsabfhldabfl124";
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

        // when
        memberService.registerMember(request);

        // then
        verify(memberRepository).existsMemberByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(memberRepository).save(any());

        ArgumentCaptor<Member> memberArgumentCaptor = ArgumentCaptor.forClass(Member.class);

        verify(memberRepository).save(memberArgumentCaptor.capture());

        Member capturedMember = memberArgumentCaptor.getValue();
        assertThat(capturedMember.getMemberId()).isNull();
        assertThat(capturedMember.getFirstName()).isEqualTo(request.firstName());
        assertThat(capturedMember.getLastName()).isEqualTo(request.lastName());
        assertThat(capturedMember.getEmail()).isEqualTo(request.email());
        assertThat(capturedMember.getPassword()).isEqualTo(encodedPassword);
        assertThat(capturedMember.getMemberRole()).isEqualTo(request.memberRole());

    }

    @Test
    void registerMember_withExistingEmail_shouldThrowException() {
        // given
        MemberRegistrationRequest request = new MemberRegistrationRequest(
                "Alex",
                "Smith",
                "test@example.com",
                "password",
                "APPRENTICE"
        );
        when(memberRepository.existsMemberByEmail(anyString())).thenReturn(true);

        // when and then
        assertThatThrownBy(() -> memberService.registerMember(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already been registered");
    }

    @Test
    void getMemberByEmail_withValidEmail_shouldReturnMemberDTO() {
        // given
        String email = "test@email.com";
        Member member = new Member(
                "Alex",
                "Smith",
                email,
                "encodedPassword",
                "APPRENTICE"
        );
        MemberDTO memberDTO = new MemberDTO(
                1,
                "Alex",
                "Smith",
                email,
                List.of("APPRENTICE"),
                email
        );
        when(memberRepository.findMemberByEmail(email))
                .thenReturn(Optional.of(member));
        when(memberDTOMapper.apply(member)).thenReturn(memberDTO);

        // when
        MemberDTO result = memberService.getMemberByEmail(email);

        // then
        verify(memberRepository).findMemberByEmail(email);
        verify(memberDTOMapper).apply(member);
        assertThat(result).isEqualTo(memberDTO);
    }

    @Test
    void getMemberByEmail_withValidEmail_shouldReturnMember() {
        // given
        String email = "test@email.com";
        Member member = new Member(
                "Alex",
                "Smith",
                email,
                "encodedPassword",
                "APPRENTICE"
        );
        when(memberRepository.findMemberByEmail(email))
                .thenReturn(Optional.of(member));

        // when
        Member result = memberService.getMemberByMemberEmail(email);

        // then
        verify(memberRepository).findMemberByEmail(email);
        assertThat(result).isEqualTo(member);
    }

    @Test
    void getMemberByEmail_withNotExistsEmail_shouldThrowException() {
        // given
        String email = "test@example.com";
        when(memberRepository.findMemberByEmail(email))
                .thenReturn(Optional.empty());

        // when and then
        assertThatThrownBy(() -> memberService.getMemberByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("member with email [%s] not found".formatted(email));

    }

    @Test
    void getAllMemberInfo_shouldReturnMemberDTOList() {
        // given
        List<Member> members = List.of(
                new Member(
                        1,
                        "Alex",
                        "Smith",
                        "test-alex@example.com",
                        "encodedPassword",
                        "APPRENTICE"
                ),
                new Member(
                        2,
                        "Connor",
                        "Chen",
                        "test-connor@example.com",
                        "encodedPassword",
                        "APPRENTICE"
                )
        );
        List<MemberDTO> memberDTOs = List.of(
                new MemberDTO(
                        1,
                        "Alex",
                        "Smith",
                        "test-alex@example.com",
                        List.of("APPRENTICE"),
                        "test-alex@example.com"
                ),
                new MemberDTO(
                        2,
                        "Connor",
                        "Chen",
                        "test-connor@example.com",
                        List.of("APPRENTICE"),
                        "test-connor@example.com"
                )
        );

        when(memberRepository.findAll()).thenReturn(members);
        when(memberDTOMapper.apply(any())).thenReturn(memberDTOs.get(0), memberDTOs.get(1));

        // when
        List<MemberDTO> result = memberService.getAllMemberInfo();

        // then
        verify(memberRepository).findAll();
        verify(memberDTOMapper, times(2)).apply(any());
        assertThat(result).containsExactlyInAnyOrderElementsOf(memberDTOs);
}

    @Test
    void checkMembersByEmailPrefix_withPrefix_shouldReturnNames() {
        // given
        String prefix = "test";
        List<Member> members = List.of(
                new Member(
                        1,
                        "Alex",
                        "Smith",
                        "test-alex@example.com",
                        "encodedPassword",
                        "APPRENTICE"
                ),
                new Member(
                        2,
                        "Connor",
                        "Chen",
                        "test-connor@example.com",
                        "encodedPassword",
                        "APPRENTICE"
                )
        );
        when(memberRepository.findTop10ByEmailStartingWith(prefix))
                .thenReturn(members);

        // when
        List<String> result = memberService.checkMembersByEmailPrefix(prefix);

        // then
        verify(memberRepository).findTop10ByEmailStartingWith(prefix);
        assertThat(result).containsExactlyInAnyOrder(
                "test-alex@example.com Alex",
                "test-connor@example.com Connor"
        );
    }
}