package ebrain.board.service;

import ebrain.board.vo.NoticeBoard;
import ebrain.board.dto.SearchConditionDTO;
import ebrain.board.mapper.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    /**
     * 게시글 저장소 객체
     */
    private final BoardRepository boardRepository;

    public List<NoticeBoard> searchNoticeBoards(SearchConditionDTO searchParamsDTO)  {
        return boardRepository.searchNoticeBoards(searchParamsDTO);
    }
}
