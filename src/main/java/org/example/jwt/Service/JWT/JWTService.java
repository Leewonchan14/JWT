package org.example.jwt.Service.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    private static final String signatureSecretKey = "c3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwtc3ByaW5nYm9vdC1qd3QtdHV0b3JpYWwK";
    private final SecretKey key = Keys.hmacShaKeyFor(signatureSecretKey.getBytes(StandardCharsets.UTF_8));

    // Create JWT Token
    public String createJWT(Long memberId) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date())
                // 30초만 유효한 토큰
                .expiration(new Date(System.currentTimeMillis() + 30000))
                .signWith(key)
                .compact();
    }

    // Header 에서 JWT Token 추출 "Authorization: Bearer {JWT}"
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
        return jws.getPayload()
                .get("memberId", Long.class);
    }


}
