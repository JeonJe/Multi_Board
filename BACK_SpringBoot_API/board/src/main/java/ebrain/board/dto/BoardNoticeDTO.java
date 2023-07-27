package ebrain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * 공지 게시글 정보를 담는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardNoticeDTO {
    /**
     * 공지 게시글 ID
     */
    private int boardId;

    /**
     * 제목
     */
    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해야 합니다.")
    private String title;

    /**
     * 내용
     */
    @NotEmpty(message = "내용은 필수 항목입니다.")
    @Size(max = 4000, message = "내용은 4000자 이하로 입력해야 합니다.")
    private String content;


    /**
     * 관리자 식별자 아이디
     */
    private int adminSeqId;


    /**
     * 작성일시
     */
    private Date createdAt;

    /**
     * 방문 횟수
     */
    private int visitCount;

    /**
     * 알림 여부 (1: 알림 표시, 0: 알림 미표시)
     */
    private int isNoticed;

    @NotEmpty(message = "카테고리 값은 필수 항목입니다.")
    private String categoryValue;


    private String categoryName;


    private String userId;





}
