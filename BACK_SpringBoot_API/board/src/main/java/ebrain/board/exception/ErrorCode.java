package ebrain.board.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode 열거형
 * 애플리케이션에서 사용되는 오류 코드를 정의한 열거형입니다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자
     */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다"),
    INVALID_AUTH_USER(HttpStatus.UNAUTHORIZED, "아이디, 비밀번호가 틀렸습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /**
     * 404 NOT_FOUND : Resource 를 찾을 수 없음
     */

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정보를 찾을 수 없습니다"),

    /**
     * 409 CONFLICT : Resource 의 현재 상태와 충돌
     */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),
    DUPLICATE_USERID(HttpStatus.CONFLICT, "이미 회원가입된 아이디입니다."),
    ;

    /**
     * HTTP 상태 코드
     */
    private HttpStatus httpStatus;
    /**
     * 오류 메시지
     */
    private String errorMessage;


}
