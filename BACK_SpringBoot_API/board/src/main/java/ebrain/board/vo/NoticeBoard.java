package ebrain.board.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeBoard {
    /**
     * 공지 게시글 ID
     */
    private int boardId;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용
     */
    private String content;

    /**
     * 작성자 ID
     */
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
     * 카테고리 값
     */
    private String categoryName;

}
