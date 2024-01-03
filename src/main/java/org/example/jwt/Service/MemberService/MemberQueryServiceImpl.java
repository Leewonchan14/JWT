package org.example.jwt.Service.MemberService;

import lombok.RequiredArgsConstructor;
import org.example.jwt.Model.DAO.Member;
import org.example.jwt.Repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberRepository memberRepository;

    @Override
    public Member findMemberById(Long id) {
        Optional<Member> optMember = memberRepository.findById(id);

        if (optMember.isEmpty())
            throw new IllegalArgumentException("Member not found" + id);

        return optMember.get();
    }

    @Override
    public Member findMemberByUsernameAndPassword(String username, String password) {
        Optional<Member> optMember = memberRepository.findByUsernameAndPassword(username, password);
        if (optMember.isEmpty())
            throw new IllegalArgumentException("Member not found" + username + password);

        return optMember.get();

    }
}
