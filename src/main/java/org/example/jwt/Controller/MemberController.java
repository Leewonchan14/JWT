package org.example.jwt.Controller;

import lombok.RequiredArgsConstructor;
import org.example.jwt.Converter.MemberConverter;
import org.example.jwt.Model.DAO.Member;
import org.example.jwt.Model.DTO.MemberRequestDTO;
import org.example.jwt.Model.DTO.MemberResponseDTO;
import org.example.jwt.Service.JWT.JWTService;
import org.example.jwt.Service.MemberService.MemberCommandService;
import org.example.jwt.Service.MemberService.MemberQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final JWTService jwtService;

    // 회원가입 Post 요청이다.
    @PostMapping("/signup")
    public MemberResponseDTO.MemberCreateResponseDTO signUp(
            // 회원가입은 username과 password를 받는다.
            @RequestBody MemberRequestDTO.MemberCreateRequestDTO request
    ) {
        // 회원가입을 하면 Member가 생성된다.
        Member newMember = memberCommandService.createMember(request);

        // 생성한 Member와 JWT를 반환한다.
        return MemberConverter.toMemberCreateResponseDTO(newMember);
    }

    // 로그인 Post 요청이다.
    @PostMapping("/login")
    public MemberResponseDTO.MemberLoginResponseDTO login(
            // 로그인은 username과 password를 받는다.
            @RequestBody MemberRequestDTO.MemberLoginRequestDTO request
    ) {
        // 로그인을 하면 username, password가 일치하는 Member를 찾는다.
        // username, password 가 일치 하지 않는 로직은 적절히 처리해야 한다.
        Member member = memberQueryService.findMemberByUsernameAndPassword(request.getUsername(), request.getPassword());

        // 로그인을 하면 JWT가 생성된다.
        String jwt = jwtService.createJWT(member.getId());

        // 생성한 Member와 JWT를 반환한다.
        return MemberConverter.toMemberLoginResponseDTO(member,jwt);
    }

    // 자동 로그인 Get 요청이다.
    @GetMapping("/auto-login")
    public MemberResponseDTO.MemberAutoLoginResponseDTO autoLogin() {
        // Header에 있는 토큰을 통해 memberId를 가져온다.
        Long memberId = jwtService.getMemberId();

        // memberId로 Member를 찾아 MemberLoginResponseDTO로 변환하여 반환한다.
        return MemberConverter.toMemberAutoLoginResponseDTO(memberQueryService.findMemberById(memberId));
    }

    // 유저 정보를 조회하는 Get 요청이다.
    @GetMapping("{memberId}")
    public MemberResponseDTO.MemberPreviewDTO findMemberById(
            // 유저 정보를 조회하려면 memberId를 받아야 한다.
            @PathVariable Long memberId
    ) {

        // Header에 있는 토큰을 통해 memberId를 가져온다.
        Long memberIdFromToken = jwtService.getMemberId();

        // 만약 토큰의 memberId와 요청의 memberId가 다르다면
        if (!memberId.equals(memberIdFromToken)) {
            // 권한이 없다는 RuntimeException을 던진다.
            throw new RuntimeException("권한이 없습니다.");
        }

        // memberId로 Member를 찾아 MemberPreviewDTO로 변환하여 반환한다.
        return MemberConverter.toMemberPreviewDTO(memberQueryService.findMemberById(memberId));
    }
}
