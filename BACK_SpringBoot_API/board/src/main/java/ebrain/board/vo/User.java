package ebrain.board.vo;

import lombok.*;

/**
 * 유저 정보를 담는 VO 클래스
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    /**
     * 사용자 식별 ID
     */
    private Integer seqId;
    /**
     * 사용자 ID
     */
    private String userId;

    /**
     * 사용자 비밀번호
     */
    private String password;

    /**
     * 사용자 이름
     */
    private String name;

}
