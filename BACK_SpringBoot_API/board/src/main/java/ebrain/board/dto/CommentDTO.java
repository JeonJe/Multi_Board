package ebrain.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 댓글 정보를 담는 DTO
 */
@Data
@Builder

public class CommentDTO {
    /**
     * 댓글 ID
     */
    private int commentId;

    /**
     * 댓글 작성자의 사용자 ID
     */
    private int userSeqId;

    /**
     * 댓글 내용
     */
    private String content;

    /**
     * 댓글 작성 일시
     */
    private Date createdAt;

    /**
     * 댓글이 속한 게시글의 ID
     */
    private int boardId;

    /**
     * 사용자명
     */
    private String userId;
}
