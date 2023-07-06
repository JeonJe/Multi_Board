package ebrain.board.mapper;

import ebrain.board.vo.NoticeBoard;
import ebrain.board.dto.SearchConditionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardRepository
{
    List<NoticeBoard> searchNoticeBoards(SearchConditionDTO searchConditionDTO);
}
