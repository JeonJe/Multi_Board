package ebrain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 문의 게시글 정보를 담는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardInquiryDTO {
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
     * 사용자 식별자 아이디
     */
    private int userSeqId;

    /**
     * 작성일시
     */
    private Date createdAt;

    /**
     * 방문 횟수
     */
    private int visitCount;

    /**
     * 카테고리 값
     */
    @NotEmpty(message = "카테고리 값은 필수 항목입니다.")
    private String categoryValue;

    /**
     * 카테고리 이름 (카테고리 목록 조회 시 사용)
     */
    private String categoryName;

    /**
     * 사용자 아이디
     */
    private String userId;
    /**
     * 비밀글 여부 (1 : 비밀 글, 0 : 일반 글)
     */
   private int isSecret;
    /**
     * 답변 여부 (1 : 답변 완료, 0 : 미 답변)
     */

   private int isAnswered;
    /**
     * 비밀글에 사용되는 비밀번호
     */
   private String password;

    /**
     * 문의 게시글 답변 리스트
     */
   List<ReplyDTO> boardReplies;


}
