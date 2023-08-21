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
     * 주어진 첨부 파일 ID에 해당하는 첨부 파일 정보를 가져오는 메서드입니다.
     *
     * @param attachmentId 첨부 파일의 고유 ID
     * @return 주어진 첨부 파일 ID에 해당하는 첨부 파일 정보를 포함하는 {@link AttachmentDTO} 객체.
     *         해당 ID에 대한 첨부 파일이 없으면 {@code null}을 반환합니다.
     */
    public AttachmentDTO getAttachmentByAttachmentId(int attachmentId){
        return attachmentRepository.getAttachmentByAttachmentId(attachmentId);
    }
    /**
     * 주어진 게시글 ID와 사용자 ID를 기반으로 첨부 파일들을 삭제하는 메서드입니다.
     * 게시글의 작성자와 현재 사용자의 ID를 비교하여 권한을 확인한 후, 첨부 파일들을 삭제합니다.
     *
     * @param seqId 게시글 작성자의 사용자 ID
     * @param boardId 삭제할 첨부 파일들이 속한 게시글의 고유 ID
     * @throws AppException 삭제 권한이 없거나 예외가 발생한 경우에 던져집니다.
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
