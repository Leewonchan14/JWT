package org.example.jwt.Service.MemberService;

import org.example.jwt.Model.DAO.Member;
import org.example.jwt.Model.DTO.MemberRequestDTO;

public interface MemberCommandService {
    Member createMember(MemberRequestDTO.MemberCreateRequestDTO request);
}
