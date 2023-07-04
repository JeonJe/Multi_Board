package ebrain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 로그인 정보를 담는 DTO
 */
@AllArgsConstructor
@Getter
public class UserLoginDTO {
    /**
     * 사용자 아이디
     */
    private String userId;

    /**
     * 비밀번호
     */
    private String password;

}
