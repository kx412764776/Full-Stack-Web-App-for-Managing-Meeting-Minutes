package com.apprenticeship.repository;

import com.apprenticeship.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is for accessing the database for member table
 */
@Repository("memberJPARepository")
public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsMemberByEmail(String email);

    Optional<Member> findMemberByEmail(String email);

}
