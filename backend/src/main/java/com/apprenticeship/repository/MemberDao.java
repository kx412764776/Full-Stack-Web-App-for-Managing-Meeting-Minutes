package com.apprenticeship.repository;

import com.apprenticeship.model.Member;

import java.util.List;


public interface MemberDao {

    List<Member> selectAllUsers();
}
