package ebrain.board.mapper;

import ebrain.board.dto.ImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageRepository {
    /**
     * 이미지 정보를 저장합니다.
     *
     * @param image 이미지 정보
     */
    void saveImage(ImageDTO image);

    /**
     * 특정 게시글에 연결된 모든 이미지 목록을 조회
     *
     * @param boardId 게시글 ID
     * @return 특정 게시글에 연결된 모든 이미지 목록을 반환합니다.
     */
    List<ImageDTO> getImagesByBoardId(int boardId);

    /**
     * 특정 게시글에 연결된 이미지들을 모두 삭제
     *
     * @param boardId 게시글 ID
     */
    void deleteImageByBoardId(int boardId);

    /**
     * 특정 이미지 ID로 이미지 정보를 조회
     *
     * @param imageId 이미지 ID
     * @return 특정 이미지 ID에 해당하는 이미지 정보를 반환합니다.
     */
    ImageDTO getImageByImageId(int imageId);

    /**
     * 이미지의 우선순위를 업데이트
     *
     * @param imageDTO 이미지 정보
     */
    void updateImagePriority(ImageDTO imageDTO);

    /**
     * 특정 게시글의 첫 번째 우선순위 이미지 정보 조회
     *
     * @param boardId 게시글 ID
     * @return 특정 게시글의 첫 번째 우선순위 이미지 정보를 반환합니다.
     */
    ImageDTO getFirstPriorityImageByBoardId(int boardId);

    /**
     * 특정 이미지 ID로 이미지를 삭제
     *
     * @param imageId 이미지 ID
     */
    void deleteImageByImageId(int imageId);
}
