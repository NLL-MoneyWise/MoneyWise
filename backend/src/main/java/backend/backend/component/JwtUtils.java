package backend.backend.component;

import backend.backend.dto.auth.JWToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;


public class JwtUtils {
    private final Key key;
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //base64로 텍스트로 저장된 시크릿 키 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); //키를 hmac-sha 알고리즘에 사용할 수 있는 형태로 변환
    }

    // JWT 토큰 생성
    public JWToken generateToken(Long userId) {
        long now = (new Date()).getTime();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId)) //JWT의 'sub' 클레임을 설정, 토큰의 주체(대상)를 나타냄(사용자Id)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME)) //'exp클레임 설정, 토큰 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256) //토큰의 서명 방식 지정, HS256알고리즘을 사용함
                .compact(); //JWT의 헤더, 페이로드, 서명을 Base64URL로 인코딩 후 이 세가지를 점(.)으로 연결 (문자열형태)

        // Refresh Token 생성
        String refreshToken = Jwts.builder() //단순히 재발급 용도이기 때문에 sub클레임이 없음
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JWToken.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) //토큰 검증 할 때 사용할 키 지정
                .build() //설정이 완료된 JWT Parser객체 생성
                .parseClaimsJws(token)//JWT토근 파싱 후 서명 검증
                .getBody(); //토큰의 claims(페이로드)를 가져옴

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("서명이 잘못되었습니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 인증입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 인증입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("잘못된 인증입니다.");
        }
    }

}
