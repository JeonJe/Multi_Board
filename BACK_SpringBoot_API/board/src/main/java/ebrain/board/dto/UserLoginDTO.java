package ebrain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * 사용자 로그인 정보를 전달하는 DTO
 */

@Data
@AllArgsConstructor
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
