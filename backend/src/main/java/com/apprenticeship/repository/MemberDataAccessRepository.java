package com.apprenticeship.repository;

import com.apprenticeship.model.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("memberRepository")
public class MemberDataAccessRepository implements MemberDao{

    private final MemberRepository memberRepository;

    public MemberDataAccessRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void insertMember(Member member) {
        memberRepository.save(member);
    }

    public boolean existsMemberByEmail(String email) {
        return memberRepository.existsMemberByEmail(email);
    }

    public boolean existsMemberById(Integer memberId) {
        return memberRepository.existsMemberById(memberId);
    }

    public Optional<Member> selectMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public Optional<Member> selectMemberById(Integer memberId) {
        return memberRepository.findMemberById(memberId);
    }
}
