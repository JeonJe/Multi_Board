package ebrain.board.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginResponse {
    private String userId;
    private String name;
    private String jwt;

}
