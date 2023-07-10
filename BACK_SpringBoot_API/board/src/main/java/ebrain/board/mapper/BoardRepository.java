package ebrain.board.mapper;

import ebrain.board.vo.NoticeBoard;
import ebrain.board.dto.SearchConditionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardRepository
{
    /**
     * 검색 조건에 해당하는 공지 게시글 목록을 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 공지 게시글 목록
     */
    List<NoticeBoard> searchNoticeBoards(SearchConditionDTO searchConditionDTO);

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
    List<NoticeBoard> getMarkedNoticedBoards();
    int countMarkedNoticedBoards();


    /**
     * 공지사항의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 공지사항의 상세 내용
     */
    NoticeBoard getNoticeBoardDetail(int boardId);
}
