package ebrain.board.mapper;

import ebrain.board.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CommentRepository 인터페이스
 * 댓글에 대한 데이터베이스 액세스를 담당하는 매퍼 인터페이스입니다.
 */
@Mapper
public interface CommentRepository {
    /**
     * 특정 자유게시판의 댓글 개수를 조회
     *
     * @param boardId 자유게시판의 ID
     * @return 댓글 개수
     */
    int countCommentByFreeBoardId(int boardId);

    /**
     * 자유게시판에 새로운 댓글을 추가
     *
     * @param commentDTO 추가할 댓글 정보를 담은 CommentDTO 객체
     */
    void addFreeBoardComment(CommentDTO commentDTO);

    /**
     * 주어진 게시글 ID에 해당하는 모든 댓글을 조회
     *
     * @param boardId 댓글을 가져올 게시글의 고유 ID
     * @return 주어진 게시글 ID에 해당하는 모든 댓글의 목록인 {@link List<CommentDTO>} 객체.
     *         게시글에 대한 댓글이 없는 경우 빈 목록을 반환합니다.
     */
    List<CommentDTO> getCommentsByBoardId(int boardId);

    /**
     * 자유게시판 댓글을 삭제
     *
     * @param commentDTO 삭제할 댓글 정보를 담은 CommentDTO 객체
     */
    void deleteFreeBoardComment(CommentDTO commentDTO);

    /**
     * 특정 댓글의 정보를 조회
     *
     * @param commentId 가져올 댓글의 고유 ID
     * @return 주어진 댓글 ID에 해당하는 댓글 정보를 포함하는 {@link CommentDTO} 객체.
     *         해당 ID에 대한 댓글이 없으면 {@code null}을 반환합니다.
     */
    CommentDTO getCommentByCommentId(int commentId);


}
