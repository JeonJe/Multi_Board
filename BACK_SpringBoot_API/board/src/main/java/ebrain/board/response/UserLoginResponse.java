package ebrain.board.response;

import lombok.Builder;
import lombok.Data;

/**
 * 유저 로그인에 대한 응답을 나타내는 클래스입니다.
 */
@Data
@Builder
public class UserLoginResponse {
    /**
     * 사용자 식별번호
     */
    private Integer seqId;
    /**
     * 사용자 아이디
     */
    private String userId;
    /**
     * 사용자 이름
     */
    private String name;
    /**
     * 사용자 JWT
     */
    private String jwt;

}
