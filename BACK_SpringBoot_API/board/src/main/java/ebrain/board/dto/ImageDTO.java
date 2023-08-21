package ebrain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 이미지 정보를 담는 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    /**
     * 이미지 파일 ID
     */
    private int imageId;
    /**
     * 서버에 저장된 중복되지 않는 파일명
     */
    private String fileName;
    /**
     * 사용자가 업로드한 파일의 원본 이름
     */
    private String originFileName;
    /**
     * 이미지 우선순위
     */
    private int priority;

    /**
     * 이미지를 포함하는 게시물의 ID
     */
    private int boardId;
}
