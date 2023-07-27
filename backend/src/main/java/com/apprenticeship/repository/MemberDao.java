package com.apprenticeship.repository;

import com.apprenticeship.model.Member;

import java.util.Optional;

public interface MemberDao {

    void insertMember(Member member);

    boolean existsMemberByEmail(String email);

    boolean existsMemberById(Integer memberId);

    Optional<Member> selectMemberByEmail(String email);

    Optional<Member> selectMemberById(Integer memberId);


}
