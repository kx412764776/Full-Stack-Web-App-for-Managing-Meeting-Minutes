package com.apprenticeship.service.MemberService;

import com.apprenticeship.model.Member;
import com.apprenticeship.repository.MemberDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(@Qualifier("userJpa") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public List<Member> selectAllUsers() {
        return memberDao.selectAllUsers();
    }
}
