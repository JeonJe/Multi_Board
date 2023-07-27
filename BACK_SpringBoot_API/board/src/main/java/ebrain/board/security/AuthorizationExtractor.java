package ebrain.board.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 요청 헤더에서 인증 정보를 추출하는 유틸리티 클래스입니다.
 */
@Component
public class AuthorizationExtractor {
    public static final String AUTHORIZATION = "Authorization";

    /**
     * 요청헤더에서 JWT 토큰을 추출
     *
     * @param request 요청을 나타내는 HttpServletRequest 객체
     * @param type    액세스 토큰 유형 ("Bearer")
     * @return 추출된 인증 토큰 또는 찾을 수 없을 경우 빈 문자열
     */
    public String extract(HttpServletRequest request, String type) {

        List<String> headers = Collections.list(request.getHeaders(AUTHORIZATION));
        for (String header : headers) {
            //지정된 타입으로 시작하는 헤더일 경우
            if (header.toLowerCase().startsWith(type.toLowerCase())) {
                //type 제외한 나머지 부분을 추출하고, 양쪽 공백을 제거하여 반환
                return header.substring(type.length()).trim();
            }
        }

        return "";
    }
}