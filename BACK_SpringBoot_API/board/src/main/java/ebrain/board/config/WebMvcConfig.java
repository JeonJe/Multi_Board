package ebrain.board.config;

import ebrain.board.security.BearerAuthInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
        registry.addInterceptor(bearerAuthInterceptor)
                .addPathPatterns("/api/auth/check")
                .addPathPatterns("/api/auth/status")
                .addPathPatterns("/api/auth/boards/**")
                .addPathPatterns("/api/boards/**")
                ;
    }

    /**
     * 메소드 수준의 유효성 검증을 활성화하기 위한 MethodValidationPostProcessor를 생성합니다.
     *
     * @return MethodValidationPostProcessor 인스턴스
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/images/thumbnail/**") // The URL path to access the thumbnails
                .addResourceLocations("file:/Users/premise/Desktop/github/Java/thumbnail/"); // The external directory path on the server

        registry.addResourceHandler("/api/images/**")
                .addResourceLocations("file:/Users/premise/Desktop/github/Java/upload/");

    }
}