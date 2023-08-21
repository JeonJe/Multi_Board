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
     * 400 BAD_REQUEST : 잘못된 요청에 대한 상태 코드와 설명입니다.
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /**
     * 401 UNAUTHORIZED : 인증되지 않은 사용자에 대한 상태 코드와 설명입니다.
     */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다"),

    /**
     * 401 UNAUTHORIZED : 유효하지 않은 사용자 인증 정보에 대한 상태 코드와 설명입니다.
     */
    INVALID_AUTH_USER(HttpStatus.UNAUTHORIZED, "아이디, 비밀번호가 틀렸습니다."),

    /**
     * 401 UNAUTHORIZED : 권한이 없는 사용자에 대한 상태 코드와 설명입니다.
     */
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /**
     * 404 NOT_FOUND : 해당 유저 정보를 찾을 수 없는 경우에 대한 상태 코드와 설명입니다.
     */
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),

    /**
     * 404 NOT_FOUND : 요청한 리소스를 찾을 수 없는 경우에 대한 상태 코드와 설명입니다.
     */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정보를 찾을 수 없습니다"),

    /**
     * 409 CONFLICT : 데이터 충돌로 인한 리소스 상태 충돌에 대한 상태 코드와 설명입니다.
     */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),

    /**
     * 409 CONFLICT : 이미 회원가입된 아이디로 인한 상태 코드와 설명입니다.
     */
    DUPLICATE_USERID(HttpStatus.CONFLICT, "이미 회원가입된 아이디입니다."),

    /**
     * 409 CONFLICT : 댓글이 남아있어 삭제가 불가능한 경우에 대한 상태 코드와 설명입니다.
     */
    REMAIN_REPLY(HttpStatus.CONFLICT, "댓글이 남아있어 삭제가 불가합니다")
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
