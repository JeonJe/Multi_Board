package ebrain.board.response;

import ebrain.board.dto.BoardFreeDTO;
import ebrain.board.dto.BoardNoticeDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;


/**
 * 검색조건에 검색결과를 나타내는 클래스입니다.
 */
@Data
@Builder
public class BoardSearchResponse {
    /**
     * 검색된 공지 게시글 목록
     */
    private List<BoardNoticeDTO> searchNoticeBoards;

    /**
     * 검색된 자유 게시글 목록
     */
    private List<BoardFreeDTO> searchFreeBoards;

    /**
     * 검색된 게시글의 총 개수
     */
    private int countSearchBoards;

    /**
     * 알림 표시된 공지 게시글 목록
     */
    private List<BoardNoticeDTO> markNoticedBoards;

}
