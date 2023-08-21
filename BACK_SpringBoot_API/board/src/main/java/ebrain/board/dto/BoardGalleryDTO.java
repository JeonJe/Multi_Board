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
 * 갤러리 게시글 정보를 담는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardGalleryDTO {

    /**
     * 게시글 ID
     */
    private int boardId;

    /**
     * 게시글 제목
     */
    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해야 합니다.")
    private String title;

    /**
     * 게시글 내용
     */
    @NotEmpty(message = "내용은 필수 항목입니다.")
    @Size(max = 4000, message = "내용은 4000자 이하로 입력해야 합니다.")
    private String content;

    /**
     * 게시글 작성자의 사용자 ID
     */
    private int userSeqId;

    /**
     * 게시글 작성일
     */
    private Date createdAt;

    /**
     * 게시글 조회수
     */
    private int visitCount;

    /**
     * 게시글 카테고리 값
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
     * 업로드된 첨부 파일 목록
     */
    private List<ImageDTO> boardImages;

    /**
     * 업로드된 이미지 파일 목록
     */
    private List<MultipartFile> uploadImages;

    /**
     * 삭제할 이미지 ID 목록
     */
    private List<Integer> deletedAttachmentIDs;

    /**
     * 썸네일 파일 경로
     */
    private String thumbnailPath;

    /**
     * 게시글과 연관된 이미지 개수
     */
    private int numOfImages;

}
