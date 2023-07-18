package ebrain.board.dto;

import lombok.*;
import org.springframework.stereotype.Service;

/**
 * 첨부파일 정보를 전달하는 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    /**
     * 첨부 파일 ID
     */
    private int attachmentId;
    /**
     * 서버에 저장된 중복되지 않는 파일명
     */

    private String fileName;
    /**
     * 사용자가 업로드한 파일의 원본 이름
     */
    private String originFileName;
    /**
     * 첨부 파일을 포함하는 게시물의 ID
     */
    private int boardId;

}
