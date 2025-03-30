package com.example.board.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long expirationTime = 1000 * 60 * 60 * 24;  // 토큰 만료 시간 (24시간)

    // JWT 토큰 생성
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // 사용자 이메일 설정
                .setIssuedAt(new Date())  // 발급 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명에 사용할 알고리즘과 비밀 키 설정
                .compact();
    }

    // JWT 토큰에서 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();  // 이메일을 subject로 가져옴
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    // JWT 토큰에서 Claims 객체 추출
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)  // 토큰을 파싱하여 Claims 객체로 변환
                    .getBody();
        } catch (MalformedJwtException e) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");  // 유효하지 않은 JWT 토큰 예외 처리
        }
    }
}
