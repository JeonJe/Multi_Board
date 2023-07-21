package ebrain.board.response;

import ebrain.board.dto.BoardDTO;
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
     * 검색 조건에 해당하는 공지 게시글 목록
     */
    private List<BoardDTO> searchBoards;
    /**
     * 검색 조건에 해당하는 공지 게시글의 개수
     */
    private int countSearchBoards;

    /**
     * 알림 표시된 게시글 목록
     */
    private List<BoardDTO> markNoticedBoards;

}
