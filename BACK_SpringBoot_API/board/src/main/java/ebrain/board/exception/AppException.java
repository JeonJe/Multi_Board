package ebrain.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AppException 클래스
 * 애플리케이션에서 발생하는 예외를 나타내는 클래스입니다.
 */
@AllArgsConstructor
@Getter
public class AppException extends RuntimeException {
    /**
     * 오류코드
     */
    private ErrorCode errorCode;
    /**
     * 예외 메시지
     */
    //TODO : message를 errorMessage로 바꾸어서 명확하게 변수명을 작성하는 것이 좋음.
    private String message;
}
