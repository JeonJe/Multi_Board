package ebrain.board.mapper;

import ebrain.board.dto.ImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageRepository {

    void saveImage(ImageDTO image);

    List<ImageDTO> getImagesByBoardId(int boardId);

    void deleteImageByBoardId(int boardId);

    ImageDTO getImageByImageId(int imageId);

    void updateImagePriority(ImageDTO imageDTO);

    void deleteImageByImageId(int imageId);
    ImageDTO getFirstPriorityImageByBoardId(int boardId);

}
