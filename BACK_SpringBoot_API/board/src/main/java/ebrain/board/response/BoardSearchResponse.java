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


    private List<BoardNoticeDTO> searchNoticeBoards;
    private List<BoardFreeDTO> searchFreeBoards;
    private int countSearchBoards;
    private List<BoardNoticeDTO> markNoticedBoards;

}
