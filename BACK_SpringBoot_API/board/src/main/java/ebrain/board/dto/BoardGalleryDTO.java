package ebrain.board.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardGalleryDTO {

    private int boardId;


    @NotEmpty(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력해야 합니다.")
    private String title;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    @Size(max = 4000, message = "내용은 4000자 이하로 입력해야 합니다.")
    private String content;


    private int userSeqId;


    private Date createdAt;


    private int visitCount;


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

    private String thumbnailPath;


}
