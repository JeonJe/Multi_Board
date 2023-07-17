package ebrain.board.dto;

import lombok.Data;

/**
 * 카테고리 정보를 담는 DTO
 */
@Data
public class CategoryDTO {
    /**
     * 카테고리 이름
     */
    private String categoryName;
    /**
     * 카테고리 값
     */
    private String categoryValue;
}
