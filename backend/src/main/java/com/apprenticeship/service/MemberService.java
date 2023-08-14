package com.apprenticeship.service;

import com.apprenticeship.dto.MemberDTO;
import com.apprenticeship.dto.MemberDTOMapper;
import com.apprenticeship.exception.DuplicateResourceException;
import com.apprenticeship.exception.ResourceNotFoundException;
import com.apprenticeship.model.Member;
import com.apprenticeship.repository.MemberRepository;
import com.apprenticeship.requestsAndResponses.MemberRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class is responsible for handling the business logic of controlling members in the database.
 */
@Service
public class MemberService {


    private final MemberRepository memberRepository;
    private final MemberDTOMapper memberDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberService(
            MemberRepository memberRepository,
            MemberDTOMapper memberDTOMapper,
            PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.memberDTOMapper = memberDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method is responsible for register a member to the database.
     */
    public void registerMember(MemberRegistrationRequest memberRegistrationRequest) {

        // check if email exist
        if (memberRepository.existsMemberByEmail(memberRegistrationRequest.email())) {
            throw new DuplicateResourceException(
                    "email already been registered"
            );
        }

        //encode password
        String encodedPassword = passwordEncoder.encode(memberRegistrationRequest.password());

        // add member to database
        Member member = new Member(
                memberRegistrationRequest.firstName(),
                memberRegistrationRequest.lastName(),
                memberRegistrationRequest.email(),
                encodedPassword,
                memberRegistrationRequest.memberRole());
        memberRepository.save(member);

    }

    /**
     * This method is responsible for getting a member from the database and return a MemberDTO object.
     * @param email member email
     * @return MemberDTO object
     */
    public MemberDTO getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .map(memberDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "member with email [%s] not found".formatted(email)
                        ));
    }

    /**
     * Get member object by email
     * @param email member email
     * @return Member object
     */
    public Member getMemberByMemberEmail(String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "member with email [%s] not found".formatted(email)
                        ));
    }

    public List<MemberDTO> getAllMemberInfo() {
        return memberRepository.findAll()
                .stream()
                .map(memberDTOMapper)
                .toList();
    }

    // check if email exists in database and return email and first name
    public List<String> checkMembersByEmailPrefix(String emailPrefix) {
        List<Member> members = memberRepository.findTop10ByEmailStartingWith(emailPrefix);
        // joint email and first name and return
        return members.stream()
                .map(member -> member.getEmail() + " " + member.getFirstName())
                .toList();
    }
}
