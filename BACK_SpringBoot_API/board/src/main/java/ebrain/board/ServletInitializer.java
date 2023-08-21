package ebrain.board;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 외부 서블릿 컨테이너에서 스프링 부트 애플리케이션을 초기화하기 위한 ServletInitializer 클래스
 * 이 클래스는 {@link SpringBootServletInitializer} 클래스를 확장하여 외부 서블릿 컨테이너에서 스프링 부트 애플리케이션을 설정합니다.
 */
public class ServletInitializer extends SpringBootServletInitializer {
    /**
     * 외부 서블릿 컨테이너에서 스프링 부트 애플리케이션의 초기화를 설정하는 메서드
     *
     * @param application 스프링 부트 애플리케이션을 구성할 {@link SpringApplicationBuilder} 객체
     * @return 초기화된 스프링 부트 애플리케이션 설정을 포함하는 {@link SpringApplicationBuilder} 객체
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BoardApplication.class);
    }

}
