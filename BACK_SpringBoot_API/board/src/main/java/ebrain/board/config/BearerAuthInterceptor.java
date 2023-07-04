package ebrain.board.config;

import ebrain.board.security.JwtTokenProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 인증 토큰을 확인하는 인터셉터 클래스입니다.
 */
@Component
@AllArgsConstructor
public class BearerAuthInterceptor implements HandlerInterceptor {
    /**
     * 요청 헤더에서 인증 정보를 추출
     */
    private AuthorizationExtractor authExtractor;
    /**
     * jwtToken 관련 유틸리티
     */
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Pre-handle 메서드는 요청이 컨트롤러에 도달하기 전에 실행되는 메서드입니다.
     * 이 메서드에서는 요청 헤더에서 JWT 토큰을 추출하고 유효성을 검사한 후,
     * 추출한 토큰의 사용자 ID를 요청 속성에 저장합니다.
     *
     * @param request  현재 요청 객체 (HttpServletRequest)
     * @param response 현재 응답 객체 (HttpServletResponse)
     * @param handler  현재 처리기 객체 (Object)
     * @return 요청 처리 여부 (true: 계속 진행, false: 중단)
     * @throws IllegalArgumentException 토큰이 유효하지 않을 경우 발생하는 예외
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        //헤더에서 JWT 토큰 추출
        String token = authExtractor.extract(request, "Bearer");

        //빈 토큰 일 경우 다음으로 이동
        if (StringUtils.isEmpty(token)) {
            return true;
        }

        //JWT 토큰이 유효하지 않는 경우 예외처리
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }

        //추출한 토큰의 사용자 ID를 요청 속성에 저장
        String userId = jwtTokenProvider.getSubject(token);
        request.setAttribute("userId", userId);
        return true;
    }
}