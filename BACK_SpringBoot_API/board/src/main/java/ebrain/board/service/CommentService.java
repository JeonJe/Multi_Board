package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.dto.CommentDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.CommentRepository;
import ebrain.board.vo.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 댓글 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    /**
     * 댓글 저장소 객체
     */
    private final CommentRepository commentRepository;
    /**
     * 자유게시판에 달린 댓글 개수를 반환합니다.
     *
     * @param boardId 게시글 ID
     * @return 댓글 개수
     */
    public int countCommentByFreeBoardId(int boardId){
        return commentRepository.countCommentByFreeBoardId(boardId);
    }

    /**
     * 자유 게시글에 댓글을 추가하는 메서드입니다.
     *
     * @param commentDTO 추가할 댓글 정보를 담은 CommentDTO 객체
     */
    public void addFreeBoardComment(CommentDTO commentDTO) {
        commentRepository.addFreeBoardComment(commentDTO);
    }

    /**
     * 자유 게시글에 달린 댓글을 삭제하는 메서드입니다.
     *
     * @param seqId 사용자 식별 ID
     * @param commentDTO 삭제할 댓글 정보를 담은 CommentDTO 객체
     * @throws AppException 삭제 권한이 없을 경우 예외가 발생합니다.
     */
    public void deleteFreeBoardComment(int seqId, CommentDTO commentDTO) {
        // 현재 userSeqId와 댓글의 userSeqId와 비교
        int commentId = commentDTO.getCommentId();
        CommentDTO comment = commentRepository.getCommentByCommentId(commentId);
        if (seqId != comment.getUserSeqId()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }

        commentRepository.deleteFreeBoardComment(commentDTO);
    }

}
