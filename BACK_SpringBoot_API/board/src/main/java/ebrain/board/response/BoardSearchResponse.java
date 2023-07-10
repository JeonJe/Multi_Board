package ebrain.board.response;

import ebrain.board.vo.NoticeBoard;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BoardSearchResponse {

    /**
     * 검색 조건에 해당하는 공지 게시글 목록
     */
    private List<NoticeBoard> searchNoticeBoards;

    /**
     * 알림 표시된 게시글 목록
     */
    private List<NoticeBoard> markNoticedBoards;

    /**
     * 검색 조건에 해당하는 공지 게시글의 개수
     */
    private int countNoticeBoards;

    /**
     * 알림 표시된 게시글의 개수
     */
    private int countMarkNoticedBoards;

}
