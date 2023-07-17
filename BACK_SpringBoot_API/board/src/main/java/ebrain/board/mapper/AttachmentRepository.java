package ebrain.board.mapper;

import ebrain.board.dto.AttachmentDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentRepository {
    void saveAttachment(AttachmentDTO attachmentDTO);
    
}
