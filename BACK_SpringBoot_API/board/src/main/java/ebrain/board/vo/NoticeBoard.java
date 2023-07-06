package ebrain.board.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeBoard {
    private int boardId;
    private String title;
    private String content;
    private String userId;
    private Date createdAt;
    private int visitCount;
    private int isNoticed;

}
