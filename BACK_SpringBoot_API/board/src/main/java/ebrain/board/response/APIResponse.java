package ebrain.board.response;

import lombok.Builder;
import lombok.Data;


/**
 * API 응답을 나타내는 클래스입니다.
 */
@Builder
@Data
public class APIResponse {
    /**
     *  정상(success), 예외(error), 오류(fail) 중 하나의 값을 가집니다.
     */
    private String status;

    /**
     * status에 따른 메시지를 나타냅니다.
     */
    private String message;

    /**
     * 정상(success)의 경우 실제 전송될 데이터를 나타냅니다.
     */
    private Object data;
}
