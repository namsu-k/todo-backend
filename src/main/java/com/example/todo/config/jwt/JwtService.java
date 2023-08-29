package com.example.todo.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String SECRET_KEY = "94ee007549b38b4899d2b69e700d92ea813616ccfbacd00d475e30b17cc16f81a050ea384893b830fcc99faec768e13933ccfd33ff75619cbb681d2a8366f7a7";

    // 토큰에서 이름 추출하기
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 모든 클레임에서 단일 클레임을 추출하는 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // 토큰 생성하기
    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims) // 클레임 정보 추가하기
            .setSubject(userDetails.getUsername()) // 서브젝트 클레임에 유저 이름 추가하기
            .setIssuedAt(new Date(System.currentTimeMillis())) // 발행한 시간 측정하기
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 만료 시간 추가하기
            .signWith(getSignInKey(), SignatureAlgorithm.HS256) // 시크릿키 적용 및 암호화 알고리즘 적용하기
            .compact(); // 토큰 변환
    }

    // 토큰 검증하기 (유저네임이 같은지 && 토큰이 만료시간이 지났는지)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 토큰 만료시간이 지났는지 검증하기
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰 만료시간 추출하기
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 토큰 모든 클레임 추출하기
    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder() // 파서 빌더를 이용해서
            .setSigningKey(getSignInKey()) // 사인인키를 가져와서 적용한다
            // 사인인키는 토큰을 발행하는데 사용되는 비밀키이다
            .build() // 파서를 빌드하고
            .parseClaimsJws(token) // 토큰을 파싱한다
            .getBody(); // 파싱한 토큰의 내용을 가져온다
    }

    private Key getSignInKey() {
        // 시크릿키를 복구한다
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // 복구한 시크릿키를 다시 암호화한다
    }
}
