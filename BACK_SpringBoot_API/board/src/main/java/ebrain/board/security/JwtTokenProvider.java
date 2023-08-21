package ebrain.board.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;


/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스입니다.
 */
@Component
public class JwtTokenProvider {

    /**
     * JWT 비밀키
     */
    private String secretKey;
    /**
     * 토큰 유효 기간(밀리초)
     */
    private long validityInMilliseconds;

    /**
     * JwtTokenProvider 생성자
     *
     * @param secretKey              시크릿 키
     * @param validityInMilliseconds 토큰의 유효 기간(밀리초)
     */
    public JwtTokenProvider(@Value("${SECRET_KEY}") String secretKey,
                            @Value("${JWT_TOKEN_EXPIRE}") long validityInMilliseconds) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }
    /**
     * 주어진 사용자 ID를 기반으로 JWT 토큰을 생성
     *
     * @param seqId 사용자의 고유 ID
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(int seqId) {
        String subject = String.valueOf(seqId);
        Claims claims = Jwts.claims().setSubject(subject);

        Date now = new Date();
        Date validity = new Date(now.getTime()
                + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 주어진 토큰에서 subject 값을 추출
     *
     * @param token 추출할 토큰
     * @return 추출된 subject 값
     */
    public String getSubject(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 주어진 토큰이 유효한지 확인
     *
     * @param token 확인할 토큰
     * @return 토큰의 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}