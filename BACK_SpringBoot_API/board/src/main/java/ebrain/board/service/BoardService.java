package ebrain.board.service;

import ebrain.board.dto.*;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;
import ebrain.board.mapper.CommentRepository;
import ebrain.board.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 게시글 서비스 클래스입니다.
 */
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
    public List<BoardNoticeDTO> searchNoticeBoards(SearchConditionDTO searchParamsDTO) {
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
    public List<BoardNoticeDTO> getMarkedNoticedBoards() {
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
    public BoardNoticeDTO getNoticeBoardDetail(int boardId) {
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
    public List<BoardFreeDTO> searchFreeBoards(SearchConditionDTO searchParamsDTO) {
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
    public BoardFreeDTO getFreeBoardDetail(int boardId) {
        boardRepository.updateFreeBoardVisitCount(boardId);

        List<AttachmentDTO> attachments = attachmentRepository.getAttachmentsByBoardId(boardId);
        List<CommentDTO> comments = commentRepository.getCommentsByBoardId(boardId);

        BoardFreeDTO boardDTO = boardRepository.getFreeBoardDetail(boardId);
        if(ObjectUtils.isEmpty(boardDTO)) {
            return null;
        }
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
    public void saveFreeBoardInfo(int seqId, BoardFreeDTO boardDTO) throws Exception {

        if (seqId <= 0) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

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

    public void updateFreeBoardInfo(int seqId, BoardFreeDTO boardDTO) throws Exception {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getFreeBoardDetail(boardDTO.getBoardId()).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "수정 권한이 없습니다.");
        }

        //게시글 수정
        boardRepository.updateFreeBoardInfo(boardDTO);

        //첨부파일 수정
        List<Integer> deletedAttachmentIds = boardDTO.getDeletedAttachmentIDs();
        if (deletedAttachmentIds != null){
            for (Integer deletedId : deletedAttachmentIds) {
                String deletedFileName = attachmentRepository.
                        getAttachmentByAttachmentId(deletedId).getFileName();

                //업로드 폴더에서 파일 삭제
                if (deletedFileName != null) {
                    File file = new File(UPLOAD_PATH + '/' + deletedFileName);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                //데이터베이스서 첨부파일 정보 삭제
                attachmentRepository.deleteAttachmentByAttachmentId(deletedId);
            }
        }

        //첨부파일 신규 추가
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

    public int hasFreeBoardEditPermission(int seqId, int boardId) {
        return boardRepository.hasFreeBoardEditPermission(seqId, boardId);
    }

    public void deleteFreeBoard(int seqId, int boardId) {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getFreeBoardDetail(boardId).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }
        boardRepository.deleteFreeBoard(boardId);
    }



}
