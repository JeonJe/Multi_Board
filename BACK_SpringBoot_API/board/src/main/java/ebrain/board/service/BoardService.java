package ebrain.board.service;

import ebrain.board.dto.*;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.AttachmentRepository;
import ebrain.board.mapper.BoardRepository;
import ebrain.board.mapper.CommentRepository;
import ebrain.board.mapper.ImageRepository;
import ebrain.board.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 게시글 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class BoardService {
    /**
     * 파일 업로드 경로
     */
    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;

    @Value("${THUMBNAIL_PATH}")
    private String THUMBNAIL_PATH;

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

    private final ImageRepository imageRepository;

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

        BoardCategory categoryParentCode = BoardCategory.NOTICE_BOARD;
        return boardRepository.getNoticeBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 자유 게시글을 검색하는 메서드입니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 검색된 자유 게시글 목록
     */
    public List<BoardFreeDTO> searchFreeBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchFreeBoards(searchParamsDTO);
    }

    /**
     * 자유 게시글을 검색 조건에 맞춰 개수를 조회하는 메서드입니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색된 자유 게시글의 개수
     */
    public int countFreeBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countFreeBoards(searchConditionDTO);
    }

    /**
     * 자유 게시글의 상세 내용을 조회하는 메서드입니다.
     * 조회할 때마다 해당 게시글의 조회수를 1 증가시킵니다.
     * 또한, 해당 게시글과 연관된 첨부 파일과 댓글 목록을 함께 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 자유 게시글 상세 내용
     */
    public BoardFreeDTO getFreeBoardDetail(int boardId) {
        BoardFreeDTO boardDTO = boardRepository.getFreeBoardDetail(boardId);
        if (ObjectUtils.isEmpty(boardDTO)) {
            return null;
        }
        boardRepository.updateFreeBoardVisitCount(boardId);

        List<AttachmentDTO> attachments = attachmentRepository.getAttachmentsByBoardId(boardId);
        List<CommentDTO> comments = commentRepository.getCommentsByBoardId(boardId);
        boardDTO.setBoardAttachments(attachments);
        boardDTO.setBoardComments(comments);

        return boardDTO;
    }

    /**
     * 자유게시판 카테고리 목록을 가져옵니다.
     *
     * @return 자유게시판 카테고리 목록
     */
    public List<CategoryDTO> getFreeBoardCategories() {

        BoardCategory categoryParentCode = BoardCategory.FREE_BOARD;
        return boardRepository.getFreeBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 자유 게시글 정보를 저장하는 메서드입니다.
     *
     * @param seqId    사용자 식별 ID
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

    /**
     * 자유 게시글 정보를 수정하는 메서드입니다.
     *
     * @param seqId    사용자 식별 ID
     * @param boardDTO 수정할 게시글 정보
     * @throws Exception 예외 발생 시
     */

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
        if (deletedAttachmentIds != null) {
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

    /**
     * 자유 게시글 수정 권한이 있는지 확인하는 메서드입니다.
     *
     * @param seqId   사용자 식별 ID
     * @param boardId 게시글 ID
     * @return 수정 권한 여부 (1: 있음, 0: 없음)
     */
    public int hasFreeBoardEditPermission(int seqId, int boardId) {
        return boardRepository.hasFreeBoardEditPermission(seqId, boardId);
    }

    /**
     * 자유 게시글을 삭제하는 메서드입니다.
     *
     * @param seqId   사용자 식별 ID
     * @param boardId 게시글 ID
     * @throws AppException 삭제 권한이 없을 경우 예외가 발생합니다.
     */
    public void deleteFreeBoard(int seqId, int boardId) {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getFreeBoardDetail(boardId).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }
        boardRepository.deleteFreeBoard(boardId);
    }

    /**
     * 갤러리게시판 카테고리 목록을 가져옵니다.
     *
     * @return 갤러리게시판 카테고리 목록
     */
    public List<CategoryDTO> getGalleryBoardCategories() {

        BoardCategory categoryParentCode = BoardCategory.GALLERY_BOARD;
        return boardRepository.getGalleryBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    public void saveGalleryBoardInfo(int seqId, BoardGalleryDTO boardDTO) throws Exception {

        if (seqId <= 0) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

        boardRepository.saveGalleryBoardInfo(boardDTO);
        List<MultipartFile> newFiles = boardDTO.getUploadImages();
        int priority = 1;

        boolean isFirstFile = true;
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {

                    String originName = file.getOriginalFilename();
                    File numberedFile = FileUtil.uploadImage(file, UPLOAD_PATH);

                    ImageDTO imagesDTO = ImageDTO.builder()
                            .boardId(boardDTO.getBoardId())
                            .fileName(numberedFile.getName())
                            .originFileName(originName)
                            .priority(priority)
                            .build();
                    imageRepository.saveImage(imagesDTO);

                    if (isFirstFile) {
                        Thumbnails.of(numberedFile)
                                .height(200)
                                .keepAspectRatio(true)
                                .toFiles(new File(THUMBNAIL_PATH), Rename.NO_CHANGE);
                    }
                    isFirstFile = false;
                    priority++;
                }
            }
        }
    }

    public List<BoardGalleryDTO> searchGalleryBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchGalleryBoards(searchParamsDTO);
    }

    public int countGalleryBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.countGalleryBoards(searchParamsDTO);
    }

    public BoardGalleryDTO getGalleryBoardDetail(int boardId) {
        BoardGalleryDTO boardDTO = boardRepository.getGalleryBoardDetail(boardId);
        if (ObjectUtils.isEmpty(boardDTO)) {
            return null;
        }

        boardRepository.updateGalleryBoardVisitCount(boardId);

        List<ImageDTO> images = imageRepository.getImagesByBoardId(boardId);

        boardDTO.setBoardImages(images);

        return boardDTO;
    }




}
