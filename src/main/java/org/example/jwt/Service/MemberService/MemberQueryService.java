package org.example.jwt.Service.MemberService;

import org.example.jwt.Model.DAO.Member;

public interface MemberQueryService {
    Member findMemberById(Long id);

    Member findMemberByUsernameAndPassword(String username, String password);
}
