package ebrain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
public class BoardDTO {
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
     * 작성자 ID
     */
    @NotEmpty(message = "작성자는 필수 항목입니다.")
    @Size(max = 255, message = "작성자는 255자 이하로 입력해야 합니다.")
    private String userId;

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

    /**
     * 카테고리 이름
     */
    @NotEmpty(message = "카테고리 값은 필수 항목입니다.")
    private String categoryValue;

    /**
     * 카테고리 값
     */
    private String categoryName;

    /**
     * 삭제할 첨부 파일의 ID 목록
     */
    private List<Integer> deletedAttachmentIDs;

    /**
     * 업로드된 첨부 파일 목록
     */
    private List<MultipartFile> uploadAttachments;

    /**
     * 업로드된 첨부 파일 목록
     */
    private List<AttachmentDTO> boardAttachments;


}
