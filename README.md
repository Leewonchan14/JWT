블로그

> [jjwt만 이용하여 로그인 구현하기](https://velog.io/@twoone14/jjwt만-이용하여-로그인-구현하기)
> 
> jjwt에 대한 설명을 먼저 보고오자
>
> [JWT란? (jjwt 라이브러리)](https://velog.io/@twoone14/JWT란-jjwt-라이브러리)

# JwtService 생성

```java
package org.example.jwt.Service.JWT;

@Service
public class JWTService {

		// 서명 비밀키로 민감정보이니 잘 관리해야 한다
    private static final String signatureSecretKey = "c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK";

		// 인코딩된 서명키가 만들어 진다.( 비밀키만 같다면 항상 같다 )
    private SecretKey key = Keys.hmacShaKeyFor(signatureSecretKey.getBytes(StandardCharsets.UTF_8));

    // Create JWT Token
    public String createJWT(Long memberId) {
        return Jwts.builder()
								// 페이로드 정보
                .claim("memberId", memberId)
								// 발행 일
                .issuedAt(new Date())
                // 하루 동안 유효
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24L))
								// 서명키로 서명
                .signWith(key)
                .compact();
    }

    // Header 에서 JWT Token 추출 "Authorization: {JWT}"
    public String getJWT() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String token = request.getHeader("Authorization");

        System.out.println("token = " + token);

        return token;
    }

    // JWT Token 을 이용해 memberId 추출
    public Long getMemberId() {
        String accessToken = getJWT();

				// 적당히 여기서 예외처리해도 된다.
        if (accessToken == null) {
            return null;
        }
        if (accessToken.isEmpty()) {
            return null;
        }

        Jws<Claims> jws;

        try {
            jws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken);
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }

				// memberId 로 걸었던 클레임을 가져온다.
        return jws.getPayload()
                .get("memberId", Long.class);
    }

}
```

# MemberController에서 JWT발급과 인증 처리하기

### Member

```java
@Entity
@Builder
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;
}
```

### MemberController

```java
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
        Member member = memberCommandService.createMember(request);
        // 회원가입을 하면 JWT가 생성된다.
        String jwt = jwtService.createJWT(member.getId());

        // 생성한 Member와 JWT를 반환한다.
        return MemberConverter.toMemberCreateResponseDTO(member,jwt);
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
```

signup

![Untitled](https://velog.velcdn.com/images/twoone14/post/e32a769c-2751-421e-9d62-53ec58655d2e/image.png)

login

![Untitled](https://velog.velcdn.com/images/twoone14/post/cb060f21-0d68-4009-863c-ce5e5c303c8e/image.png)

get member info

Authorization 헤더값을 넣어야 인증된다.

![Untitled](https://velog.velcdn.com/images/twoone14/post/394d67d5-d0b2-4090-bdd5-1cd684e068d0/image.png)

만약 권한이 없거나 유효시간이 지났다면

아래와 같이 에러가 발생한다.

![Untitled](https://velog.velcdn.com/images/twoone14/post/00a93099-b26a-4355-afa2-ba2e8539afe8/image.png)

![Untitled](https://velog.velcdn.com/images/twoone14/post/64d1f6fe-dc7f-48fc-ba71-88ccc0c26c7a/image.png)