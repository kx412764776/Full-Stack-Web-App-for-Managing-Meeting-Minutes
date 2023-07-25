package com.apprenticeship.repository;

import com.apprenticeship.model.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userJpa")
public class MemberJPADataAccessService implements MemberDao {

    private final MemberRepository memberRepository;

    public MemberJPADataAccessService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> selectAllUsers() {
        return memberRepository.findAll();
    }
}
