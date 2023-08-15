package ebrain.board.mapper;

import ebrain.board.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BoardRepository 인터페이스
 * 게시글에 대한 데이터베이스 액세스를 담당하는 매퍼 인터페이스입니다.
 */

@Mapper
public interface BoardRepository {
    /**
     * 공지 게시글 목록을 검색
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 공지 게시글 목록
     */
    List<BoardNoticeDTO> searchNoticeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 공지 게시글의 개수를 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 공지 게시글의 개수
     */
    int countNoticeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 알림 표시된 공지 게시글 목록을 조회
     *
     * @return 알림 표시된 게시글 목록
     */
    List<BoardNoticeDTO> getMarkedNoticedBoards();

    /**
     * 알림 표시된 공지 게시글의 개수를 조회
     *
     * @return 알림 표시된 공지 게시글의 개수
     */
    int countMarkedNoticedBoards();

    /**
     * 공지사항의 상세 내용을 조회
     *
     * @param boardId 게시글 ID
     * @return 공지사항의 상세 내용
     */
    BoardNoticeDTO getNoticeBoardDetail(int boardId);

    /**
     * 공지사항의 카테고리 목록을 조회
     *
     * @param categoryParentValue 카테고리 부모 값
     * @return 카테고리 목록
     */
    List<CategoryDTO> getNoticeBoardCategories(String categoryParentValue);

    /**
     * 공지사항의 조회수를 1 증가
     *
     * @param boardId 게시글 ID
     */
    void updateNoticeBoardVisitCount(int boardId);

    /**
     * 각 게시판 종류별 최신 공지 게시글 목록을 조회
     *
     * @param amount 조회할 최신 공지 게시글의 개수
     * @return 최신 공지 게시글 목록
     */
    List<BoardNoticeDTO> getRecentNoticeBoards(int amount);

    /**
     * 검색 조건에 해당하는 자유 게시글 목록을 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 자유 게시글 목록
     */
    List<BoardFreeDTO> searchFreeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 자유 게시글의 개수를 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 자유 게시글의 개수
     */
    int countFreeBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 자유 게시글의 상세 내용을 조회
     *
     * @param boardId 게시글 ID
     * @return 자유게시글 상세 내용
     */
    BoardFreeDTO getFreeBoardDetail(int boardId);

    /**
     * 자유게시판의 카테고리 목록을 조회
     *
     * @param categoryParentValue 카테고리 부모 값
     * @return 카테고리 목록
     */
    List<CategoryDTO> getFreeBoardCategories(String categoryParentValue);

    /**
     * 자유게시판의 조회수를 1 증가
     *
     * @param boardId 게시글 ID
     */
    void updateFreeBoardVisitCount(int boardId);

    /**
     * 새로운 자유게시판을 저장
     *
     * @param boardDTO 자유게시판 정보 DTO
     * @return 저장된 게시글의 ID
     */
    int saveFreeBoardInfo(BoardFreeDTO boardDTO);

    /**
     * 자유게시판 수정 권한 여부를 확인
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @return 권한 여부 (1: 권한 있음, 0: 권한 없음)
     */
    int hasFreeBoardEditPermission(int seqId, int boardId);

    /**
     * 자유게시판을 삭제
     *
     * @param boardId 게시글 ID
     */
    void deleteFreeBoard(int boardId);

    /**
     * 자유게시판을 수정
     *
     * @param boardDTO 수정할 자유게시판 정보 DTO
     */
    void updateFreeBoardInfo(BoardFreeDTO boardDTO);


    /**
     * 각 게시판 종류별 최신 자유 게시글 목록을 조회
     *
     * @param amount 조회할 최신 자유 게시글의 개수
     * @return 최신 자유 게시글 목록
     */
    List<BoardFreeDTO> getRecentFreeBoards(int amount);

    /**
     * 갤러리게시판의 카테고리 목록을 조회
     *
     * @param categoryParentValue 카테고리 부모 값
     * @return 카테고리 목록
     */
    List<CategoryDTO> getGalleryBoardCategories(String categoryParentValue);

    /**
     * 갤러리게시판 정보를 저장
     *
     * @param boardDTO 갤러리게시판 정보 DTO
     * @return 저장된 게시글의 ID
     */
    int saveGalleryBoardInfo(BoardGalleryDTO boardDTO);

    /**
     * 갤러리 게시글을 수정합니다.
     *
     * @param boardDTO 수정할 문의 게시글 정보 DTO
     */
    void updateGalleryBoardInfo(BoardGalleryDTO boardDTO);

    /**
     * 검색 조건에 해당하는 갤러리 게시글 목록을 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 갤러리 게시글 목록
     */
    List<BoardGalleryDTO> searchGalleryBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 갤러리 게시글의 개수를 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 갤러리 게시글의 개수
     */
    int countGalleryBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 갤러리게시판의 상세 내용을 조회
     *
     * @param boardId 게시글 ID
     * @return 갤러리게시판 상세 내용
     */
    BoardGalleryDTO getGalleryBoardDetail(int boardId);

    /**
     * 갤러리게시판의 조회수를 1 증가
     *
     * @param boardId 게시글 ID
     */
    void updateGalleryBoardVisitCount(int boardId);

    /**
     * 갤러리게시판 수정 권한 여부를 확인
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @return 권한 여부 (1: 권한 있음, 0: 권한 없음)
     */
    int hasGalleryBoardEditPermission(int seqId, int boardId);

    /**
     * 갤러리게시판을 삭제합니다.
     *
     * @param boardId 게시글 ID
     */
    void deleteGalleryBoard(int boardId);

    /**
     * 각 게시판 종류별 최신 갤러리 게시글 목록을 조회
     *
     * @param amount 조회할 최신 갤러리 게시글의 개수
     * @return 최신 갤러리 게시글 목록
     */
    List<BoardGalleryDTO> getRecentGalleryBoards(int amount);


    /**
     * 문의게시판의 카테고리 목록을 조회
     *
     * @param categoryParentValue 카테고리 부모 값
     * @return 카테고리 목록
     */
    List<CategoryDTO> getInquiryBoardCategories(String categoryParentValue);

    /**
     * 검색 조건에 해당하는 문의 게시글 목록을 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색 결과 문의 게시글 목록
     */
    List<BoardInquiryDTO> searchInquiryBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 검색 조건에 해당하는 문의 게시글의 개수를 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 문의 게시글의 개수
     */
    int countInquiryBoards(SearchConditionDTO searchConditionDTO);

    /**
     * 새로운 문의 게시글을 저장
     *
     * @param boardInquiryDTO 문의 게시글 정보 DTO
     */
    void saveInquiryBoardInfo(BoardInquiryDTO boardInquiryDTO);

    /**
     * 문의 게시글 수정 권한 여부를 확인
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @return 권한 여부 (1: 권한 있음, 0: 권한 없음)
     */
    int hasInquiryBoardEditPermission(int seqId, int boardId);

    /**
     * 문의 게시글의 상세 내용을 조회
     *
     * @param boardId 게시글 ID
     * @return 문의 게시글 상세 내용
     */
    BoardInquiryDTO getInquiryBoardDetail(int boardId);

    /**
     * 문의 게시글의 조회수를 1 증가
     *
     * @param boardId 게시글 ID
     */
    void updateInquiryBoardVisitCount(int boardId);

    /**
     * 문의 게시글을 삭제합니다.
     *
     * @param boardId 게시글 ID
     */
    void deleteInquiryBoard(int boardId);

    /**
     * 문의 게시글을 수정합니다.
     *
     * @param boardDTO 수정할 문의 게시글 정보 DTO
     */
    void updateInquiryBoardInfo(BoardInquiryDTO boardDTO);

    /**
     * 각 게시판 종류별 최신 문의 게시글 목록을 조회
     *
     * @param amount 조회할 최신 문의 게시글의 개수
     * @return 최신 문의 게시글 목록
     */
    List<BoardInquiryDTO> getRecentInquiryBoards(int amount);

}
