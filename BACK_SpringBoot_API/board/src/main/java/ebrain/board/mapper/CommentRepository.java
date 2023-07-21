package ebrain.board.mapper;

import ebrain.board.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentRepository {

    int countCommentByFreeBoardId(int boardId);

    void addFreeBoardComment(CommentDTO commentDTO);

    List<CommentDTO> getCommentsByBoardId(int boardId);

    void deleteFreeBoardComment(CommentDTO commentDTO);


}
