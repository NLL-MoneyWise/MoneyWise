package backend.backend.security.jwt;

import backend.backend.exception.AuthException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private final Key key;
    private static final String BEARER_TYPE = "Bearer";

    //토큰은 밀리초 단위로 계산해야한다. 쿠키는 초단위이다.
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 180;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    public JwtUtils(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //base64로 텍스트로 저장된 시크릿 키 디코딩
        this.key = Keys.hmacShaKeyFor(keyBytes); //키를 hmac-sha 알고리즘에 사용할 수 있는 형태로 변환
    }

    // JWT 토큰 생성
    public String generateAccessToken(String email, String name, String nickname) {
        long now = new Date().getTime(); //JJWT의 메서드는 Date만 지원한다. (LocalDateTime사용 불가)

        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .claim("nickname", nickname)
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String email) {
        long now = new Date().getTime();

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key)
                .compact();
    }

    public String getUserEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) //토큰 검증 할 때 사용할 키 지정
                .build() //설정이 완료된 JWT Parser객체 생성
                .parseClaimsJws(token)//JWT토근 파싱 후 서명 검증
                .getBody(); //토큰의 claims(페이로드)를 가져옴

        return claims.getSubject();
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("name", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            throw new AuthException("잘못된 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new AuthException("만료된 인증 입니다.");
        } catch (UnsupportedJwtException e) {
            throw new AuthException("지원하지 않는 인증입니다.");
        } catch (IllegalArgumentException e) {
            throw new AuthException("잘못된 인증입니다.");
        }
    }
}
