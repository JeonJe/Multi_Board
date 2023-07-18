package ebrain.board.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 사용자 회원 가입 정보를 전달하는 DTO
 */

@Data
@AllArgsConstructor
public class UserSignupDTO {
    /**
     * 사용자 ID
     */
    @NotEmpty(message = "ID는 필수 항목입니다")
    @Size(min = 4, max = 11, message = "ID는 4자 이상 11자 이하로 입력해야 합니다")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "ID는 영문자, 숫자, '-', '_'만 사용할 수 있습니다")
    private String userId;

    /**
     * 사용자 비밀번호
     */
    @NotEmpty(message = "비밀번호는 필수 항목입니다")
    @Size(min = 4, max = 11, message = "비밀번호는 4자 이상 11자 이하로 입력해야 합니다")
    @Pattern(regexp = "^(?!.*([A-Za-z0-9#?!@$%^&*-])\\1{2})[A-Za-z0-9#?!@$%^&*-]+$", message = "비밀번호는 영어 소문자, 대문자, 숫자, 특수문자 중 3개 이상 연속된 문자를 포함할 수 없습니다")
    private String password;

    /**
     * 사용자 비밀번호 확인
     */
    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다")
    @Size(min = 4, max = 11, message = "비밀번호 확인은 4자 이상 11자 이하로 입력해야 합니다")
    @Pattern(regexp = "^(?!.*([A-Za-z0-9#?!@$%^&*-])\\1{2})[A-Za-z0-9#?!@$%^&*-]+$", message = "비밀번호 확인은 영어 소문자, 대문자, 숫자, 특수문자 중 3개 이상 연속된 문자를 포함할 수 없습니다")
    private String confirmPassword;

    /**
     * 사용자 이름
     */
    @NotEmpty(message = "이름은 필수 항목입니다")
    @Size(min = 2, max = 4, message = "이름은 2자 이상 4자 이하로 입력해야 합니다")
    private String name;

    /**
     * ID와 비밀번호가 동일한지 확인하는 메서드입니다.
     *
     * @return ID와 비밀번호가 동일한 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    @AssertFalse(message = "아이디와 같은 비밀번호는 사용할 수 없습니다.")
    public boolean isIdPasswordSame() {
        return userId.equals(password);
    }
    /**
     * isPasswordSame 메서드
     * 비밀번호와 확인용 비밀번호가 동일한지 확인하는 메서드입니다.
     *
     * @return 비밀번호와 확인용 비밀번호가 동일한 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    @AssertTrue(message = "비밀번호와 비밀번호 확인이 동일하지 않습니다.")
    public boolean isPasswordSame() {
        return password.equals(confirmPassword);
    }

}
