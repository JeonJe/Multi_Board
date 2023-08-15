package ebrain.board.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 댓글 정보를 담는 DTO
 */
@Data
@Builder

public class ReplyDTO {
    /**
     * 답변 번호
     */
    private int replyId;

    /**
     * 댓글 작성자의 사용자 ID
     */
    private int adminSeqId;

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
     * 답변한 관리자 아이디
     */
    private String adminId;
}
