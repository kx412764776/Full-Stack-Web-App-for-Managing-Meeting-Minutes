package com.apprenticeship.repository;

import com.apprenticeship.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface is for accessing the database for member table
 */
@Repository("memberJPARepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsMemberByEmail(String email);

    Optional<Member> findMemberByEmail(String email);

    // According to the email prefix to find members
    List<Member> findTop10ByEmailStartingWith(String emailPrefix);

    Optional<Member> findMemberByMemberId(Integer memberId);

}
