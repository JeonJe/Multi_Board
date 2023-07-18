package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.dto.CommentDTO;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.CommentRepository;
import lombok.RequiredArgsConstructor;
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



}
