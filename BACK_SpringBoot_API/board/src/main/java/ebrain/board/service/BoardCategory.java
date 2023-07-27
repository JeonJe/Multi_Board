package ebrain.board.service;

import lombok.Getter;

/**
 * 게시글의 카테고리를 정의하는 열거형(Enum) 클래스
 */
@Getter
public enum BoardCategory {
    /**
     * 공지사항 카테고리
     */
    FREE_BOARD("자유게시판", "free"),
    /**
     * 공지사항 카테고리
     */
    NOTICE_BOARD("공지사항", "notice"),

    /**
     * 갤러리게시판 카테고리
     */
    GALLERY_BOARD("갤러리게시판", "gallery"),

    /**
     * 문의게시판 카테고리
     */
    INQUIRY_BOARD("문의게시판", "inquiry");

    /**
     * 카테고리의 부모 코드 테이블 이름
     */
    private final String categoryParentCodeName;

    /**
     * 카테고리의 부모 코드 테이블 값
     */
    private final String categoryParentCodeValue;

    /**
     * BoardCategory 생성자
     *
     * @param categoryParentCodeName 카테고리의 부모 코드 테이블 이름
     * @param categoryParentCodeValue 카테고리의 부모 코드 테이블 값
     */
    BoardCategory(String categoryParentCodeName, String categoryParentCodeValue) {
        this.categoryParentCodeName = categoryParentCodeName;
        this.categoryParentCodeValue = categoryParentCodeValue;
    }

}
