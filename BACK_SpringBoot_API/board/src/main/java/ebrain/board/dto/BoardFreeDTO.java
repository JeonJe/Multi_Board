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
 * 자유 게시글 정보를 담는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardFreeDTO {
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
     * 카테고리 이름
     */
    @NotEmpty(message = "카테고리 값은 필수 항목입니다.")
    private String categoryValue;

    /**
     * 카테고리 이름 (카테고리 목록 조회 시 사용)
     */
    private String categoryName;

    /**
     * 사용자 아이디 (사용자 명으로 대체하는 것이 좋습니다.)
     */
    private String userId;

    /**
     * 업로드된 첨부 파일 목록
     */
    private List<AttachmentDTO> boardAttachments;

    /**
     * 첨부 파일 개수
     */
    private int countBoardAttachment;

    /**
     * 업로드된 첨부 파일 목록 (파일 업로드 시 사용)
     */
    private List<MultipartFile> uploadAttachments;

    /**
     * 삭제할 첨부 파일의 ID 목록 (파일 삭제 시 사용)
     */
    private List<Integer> deletedAttachmentIDs;

    /**
     * 댓글 목록
     */
    private List<CommentDTO> boardComments;

    /**
     * 댓글 개수
     */
    private int countBoardComment;


}
