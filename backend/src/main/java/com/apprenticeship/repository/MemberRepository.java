package com.apprenticeship.repository;

import com.apprenticeship.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsMemberByEmail(String email);

    boolean existsMemberById(Integer memberId);
    Optional<Member> findMemberByEmail(String email);

    Optional<Member> findMemberById(Integer memberId);
}
