package ebrain.board.mapper;

import ebrain.board.dto.BoardDTO;
import ebrain.board.dto.CategoryDTO;
import ebrain.board.dto.SearchConditionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * BoardRepository 인터페이스
 * 게시글에 대한 데이터베이스 액세스를 담당하는 매퍼 인터페이스입니다.
 */

@Mapper
public interface BoardRepository {
    /**
     * 검색 조건에 해당하는 공지 게시글 목록을 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 공지 게시글 목록
     */
    List<BoardDTO> searchNoticeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 공지 게시글의 개수를 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 공지 게시글의 개수
     */
    int countNoticeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 알림 표시된 공지 게시글 목록을 조회합니다.
     *
     * @return 알림 표시된 게시글 목록
     */
    List<BoardDTO> getMarkedNoticedBoards();

    /**
     * 알림 표시된 공지 게시글의 개수를 조회합니다.
     *
     * @return 알림 표시된 공지 게시글의 개수
     */

    int countMarkedNoticedBoards();


    /**
     * 공지사항의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 공지사항의 상세 내용
     */
    BoardDTO getNoticeBoardDetail(int boardId);

    /**
     * 공지사항의 카테고리 목록을 가져옵니다.
     *
     * @return 카테고리 목록
     */
    List<CategoryDTO> getNoticeBoardCategories();

    /**
     * 공지사항의 조회수를 1 증가시킵니다.
     * @param boardId 게시글 ID
     */
    void updateNoticeBoardVisitCount(int boardId);

    /**
     * 검색 조건에 해당하는 자유 게시글 목록을 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 자유 게시글 목록
     */
    List<BoardDTO> searchFreeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 자유 게시글의 개수를 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 자유 게시글의 개수
     */
    int countFreeBoards(SearchConditionDTO searchConditionDTO);

    /**
     *  자유 게시글의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 자유게시글 상세 내용
     */
    BoardDTO getFreeBoardDetail(int boardId);

    /**
     * 자유게시판의 카테고리 목록을 가져옵니다.
     *
     * @return 카테고리 목록
     */
    List<CategoryDTO> getFreeBoardCategories();

    /**
     * 공지사항의 조회수를 1 증가시킵니다.
     * @param boardId 게시글 ID
     */
    void updateFreeBoardVisitCount(int boardId);

    /**
     * 새로운 자유게시판을 저장합니다.
     * @param boardDTO
     * @return
     */
    int saveFreeBoardInfo(BoardDTO boardDTO);

    int hasFreeBoardEditPermission(int seqId, int boardId);

    void deleteFreeBoard(int boardId);
    /**
     * 자유게시판 정보를 업데이트하는 메서드입니다.
     *
     * @param boardDTO 업데이트할 자유게시판 정보를 담은 BoardDTO 객체
     */
    void updateFreeBoardInfo(BoardDTO boardDTO);

}
