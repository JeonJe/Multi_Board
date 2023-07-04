package ebrain.board.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 동작을 커스텀하기 위한 설정 클래스입니다.
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 헤더에서 JWT 토큰을 추출하기 위한 인터셉터
     */
    private final BearerAuthInterceptor bearerAuthInterceptor;

    /**
     * 애플리케이션의 InterceptorRegistry에 사용자 정의 인터셉터를 추가합니다.
     * 인터셉터는 들어오는 요청과 나가는 응답을 처리하는 데 사용됩니다.
     * 이 설정에서는 BearerAuthInterceptor를 특정 URL의 JWT 토큰 확인을 위해 등록합니다.
     *
     * @param registry BearerAuthInterceptor가 추가될 InterceptorRegistry 인스턴스
     */
    public void addInterceptors(InterceptorRegistry registry) {
        //JWT 토큰 확인을 위한 인터셉터 등록
        registry.addInterceptor(bearerAuthInterceptor).addPathPatterns("/api/auth/check");
    }
}