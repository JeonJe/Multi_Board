package ebrain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {
    private String userId;
    private String password;

}
