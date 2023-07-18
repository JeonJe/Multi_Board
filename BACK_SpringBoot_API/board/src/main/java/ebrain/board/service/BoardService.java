package ebrain.board.service;

import ebrain.board.dto.*;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;
import ebrain.board.mapper.CommentRepository;
import ebrain.board.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    /**
     * 파일 업로드 경로
     */
    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;

    /**
     * 게시글 저장소 객체
     */
    private final BoardRepository boardRepository;

    /**
     * 첨부파일 저장소 객체
     */
    private final AttachmentRepository attachmentRepository;

    /**
     * 댓글 저장소 객체
     */
    private final CommentRepository commentRepository;

    /**
     * 검색 조건에 해당하는 공지 게시글 목록을 조회합니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 공지 게시글 목록
     */
    public List<BoardDTO> searchNoticeBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchNoticeBoards(searchParamsDTO);
    }

    /**
     * 검색 조건에 해당하는 공지 게시글의 개수를 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 공지 게시글의 개수
     */
    public int countNoticeBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countNoticeBoards(searchConditionDTO);
    }

    /**
     * 알림 표시된 공지 게시글 목록을 조회합니다.
     *
     * @return 알림 표시된 게시글 목록
     */
    public List<BoardDTO> getMarkedNoticedBoards() {
        return boardRepository.getMarkedNoticedBoards();
    }

    /**
     * 알림 표시된 공지 게시글의 개수를 조회합니다.
     *
     * @return 알림 표시된 게시글의 개수
     */
    public int countMarkedNoticedBoards() {
        return boardRepository.countMarkedNoticedBoards();
    }

    /**
     * 공지사항의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 공지사항의 상세 내용
     */
    public BoardDTO getNoticeBoardDetail(int boardId) {
        boardRepository.updateNoticeBoardVisitCount(boardId);
        return boardRepository.getNoticeBoardDetail(boardId);
    }

    /**
     * 공지사항의 카테고리 목록을 가져옵니다.
     *
     * @return 공지사항의 카테고리 목록
     */
    public List<CategoryDTO> getNoticeBoardCategories() {
        return boardRepository.getNoticeBoardCategories();
    }

    /**
     * 검색 조건에 해당하는 자유 게시글 목록을 조회합니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 자유 게시글 목록
     */
    public List<BoardDTO> searchFreeBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchFreeBoards(searchParamsDTO);
    }

    /**
     * 검색 조건에 해당하는 자유 게시글의 개수를 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 공지 게시글의 개수
     */
    public int countFreeBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countFreeBoards(searchConditionDTO);
    }

    /**
     * 자유게시글의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 자유게시글 상세 내용
     */
    public BoardDTO getFreeBoardDetail(int boardId) {
        boardRepository.updateFreeBoardVisitCount(boardId);
        List<AttachmentDTO> attachments = attachmentRepository.getAttachmentsByBoardId(boardId);
        List<CommentDTO> comments = commentRepository.getCommentsByBoardId(boardId);

        //TODO : builder 패턴으로 바꿀 수 있을까?
        BoardDTO boardDTO = boardRepository.getFreeBoardDetail(boardId);
        boardDTO.setBoardAttachments(attachments);
        boardDTO.setBoardComments(comments);

        return boardDTO;

    }

    /**
     * 공지사항의 카테고리 목록을 가져옵니다.
     *
     * @return 공지사항의 카테고리 목록
     */
    public List<CategoryDTO> getFreeBoardCategories() {
        return boardRepository.getFreeBoardCategories();
    }

    /**
     * 자유 게시글 정보를 저장합니다.
     *
     * @param boardDTO 저장할 게시글 정보
     * @throws Exception 예외 발생 시
     */
    public void saveFreeBoardInfo(BoardDTO boardDTO) throws Exception {

        boardRepository.saveFreeBoardInfo(boardDTO);
        List<MultipartFile> newFiles = boardDTO.getUploadAttachments();

        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String originName = file.getOriginalFilename();
                    String numberedFileName = FileUtil.uploadFile(file, UPLOAD_PATH).getName();
                    AttachmentDTO attachmentDTO = AttachmentDTO.builder()
                            .boardId(boardDTO.getBoardId())
                            .fileName(numberedFileName)
                            .originFileName(originName)
                            .build();
                    attachmentRepository.saveAttachment(attachmentDTO);
                }
            }
        }
    }

    public int hasFreeBoardEditPermission(String userId, int boardId) {
        return boardRepository.hasFreeBoardEditPermission(userId, boardId);
    }

    public void deleteFreeBoard(String userId, int boardId) {
        boardRepository.deleteFreeBoard(userId, boardId);
    }

}
