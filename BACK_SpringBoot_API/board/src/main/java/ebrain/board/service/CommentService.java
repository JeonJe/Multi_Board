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

@Service
@RequiredArgsConstructor
public class CommentService {
    /**
     * 댓글 저장소 객체
     */
    private final CommentRepository commentRepository;

    public int countCommentByFreeBoardId(int boardId){
        return commentRepository.countCommentByFreeBoardId(boardId);
    }

    public void addFreeBoardComment(CommentDTO commentDTO){
        commentRepository.addFreeBoardComment(commentDTO);
    }

    public void deleteFreeBoardComment(String userId, CommentDTO commentDTO) {
        if (StringUtils.isEmpty(userId) || !userId.equals(commentDTO.getUserId())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

        commentRepository.deleteFreeBoardComment(commentDTO);
    }



}
