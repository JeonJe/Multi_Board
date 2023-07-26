package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 첨부파일 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AttachmentService {
    /**
     * 첨부파일 저장소 객체
     */
    private final AttachmentRepository attachmentRepository;

    private  final BoardRepository boardRepository;

    /**
     * 첨부파일 ID로 첨부파일 정보를 가져옵니다.
     * @param attachmentId
     * @return
     */
    public AttachmentDTO getAttachmentByAttachmentId(int attachmentId){
        return attachmentRepository.getAttachmentByAttachmentId(attachmentId);
    }
    /**
     * 특정 자유게시판의 첨부 파일을 삭제합니다.
     *
     * @param boardId 자유게시판의 ID
     */
    public void deleteAttachmentsByBoardId(int seqId, int boardId) {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getFreeBoardDetail(boardId).getUserSeqId();

        if(seqId != getUserSeqId) {
            new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }
        attachmentRepository.deleteAttachmentsByBoardId(boardId);
    }
}
