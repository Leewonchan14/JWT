package org.example.jwt.Service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.jwt.Converter.MemberConverter;
import org.example.jwt.Model.DAO.Member;
import org.example.jwt.Model.DTO.MemberRequestDTO;
import org.example.jwt.Repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService{
    private final MemberRepository memberRepository;
    @Override
    public Member createMember(MemberRequestDTO.MemberCreateRequestDTO request) {
        return memberRepository.save(MemberConverter.toMember(request));
    }
}
