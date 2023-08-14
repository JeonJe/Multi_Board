package ebrain.board.mapper;

import ebrain.board.dto.CommentDTO;
import ebrain.board.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ReplyRepository {
    /**
     * 특정 게시글에 달린 모든 댓글을 조회
     *
     * @param boardId 게시글 ID
     * @return 특정 게시글에 달린 댓글 목록
     */
    List<ReplyDTO> getRepliesByBoardId(int boardId);

    /**
     * 특정 게시글에 달린 댓글의 개수를 조회
     *
     * @param boardId 게시글 ID
     * @return 특정 게시글에 달린 댓글 개수
     */
    int countRepliesByBoardId(int boardId);

}
