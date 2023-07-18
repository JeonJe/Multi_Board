package ebrain.board.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
@Data
@Builder

public class CommentDTO {
    private int commentId;
    private String userId;
    private String content;
    private Date createdAt;
    private int boardId;
}
