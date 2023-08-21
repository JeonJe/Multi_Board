package ebrain.board.mapper;

import ebrain.board.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ReplyRepository 인터페이스
 * 문의 답변에 대한 데이터베이스 액세스를 담당하는 매퍼 인터페이스입니다.
 */
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
