package ebrain.board.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 검색 조건을 전달하는 DTO
 */
@Data
public class SearchConditionDTO {
    /**
     * 검색할 카테고리의 값
     */
    private String categoryValue;

    /**
     * 검색 키워드
     */
    private String searchText;

    /**
     * 검색 시작 날짜
     */
    private LocalDate startDate;

    /**
     * 검색 종료 날짜
     */
    private LocalDate endDate;

    /**
     * 페이지 사이즈
     */
    private Integer pageSize;

    /**
     * 현재페이지
     */
    private Integer currentPage;

    /**
     * 페이지 offset
     */
    private Integer offset;

    /**
     * 정렬 기준(예 : 등록 일시)
     */
    private String sortCriteria;

    /**
     * 정렬 순서(예 desc ..)
     */
    private String orderBy;
}
