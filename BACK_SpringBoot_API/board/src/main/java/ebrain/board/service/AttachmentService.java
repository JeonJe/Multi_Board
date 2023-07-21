package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.mapper.AttachmentRepository;
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
    public void deleteAttachmentsByBoardId(int boardId) {
        attachmentRepository.deleteAttachmentsByBoardId(boardId);
    }
}
