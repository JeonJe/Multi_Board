package ebrain.board.mapper;

import ebrain.board.dto.CommentDTO;
import ebrain.board.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ReplyRepository {
    List<ReplyDTO> getRepliesByBoardId(int boardId);
    int countRepliesByBoardId(int boardId);


}
