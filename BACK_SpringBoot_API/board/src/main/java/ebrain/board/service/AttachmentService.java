package ebrain.board.service;

import ebrain.board.dto.AttachmentDTO;
import ebrain.board.mapper.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    /**
     * 첨부파일 저장소 객체
     */
    private final AttachmentRepository attachmentRepository;

    public AttachmentDTO getAttachmentByAttachmentId(int attachmentId){
        return attachmentRepository.getAttachmentByAttachmentId(attachmentId);
    }
}
