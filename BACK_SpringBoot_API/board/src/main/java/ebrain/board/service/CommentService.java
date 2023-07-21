package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.dto.CommentDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.CommentRepository;
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
     * 자유게시판에 댓글을 추가합니다.
     *
     * @param userId     사용자 ID
     * @param commentDTO 댓글 정보를 담은 DTO 객체
     * @throws AppException 사용자 정보가 유효하지 않을 경우 예외 발생
     */
    public void addFreeBoardComment(String userId, CommentDTO commentDTO){
        if (StringUtils.isEmpty(userId) || !userId.equals(commentDTO.getUserId())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }
            commentRepository.addFreeBoardComment(commentDTO);
    }
    /**
     * 자유게시판의 댓글을 삭제합니다.
     *
     * @param userId     사용자 ID
     * @param commentDTO 댓글 정보를 담은 DTO 객체
     * @throws AppException 사용자 정보가 유효하지 않을 경우 예외 발생
     */
    public void deleteFreeBoardComment(String userId, CommentDTO commentDTO) {
        if (StringUtils.isEmpty(userId) || !userId.equals(commentDTO.getUserId())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

        commentRepository.deleteFreeBoardComment(commentDTO);
    }



}
