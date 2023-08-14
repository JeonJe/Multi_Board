package ebrain.board.service;

import ebrain.board.dto.*;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.*;
import ebrain.board.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    /**
     * 썸네일 업로드 경로
     */
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

    private final ReplyRepository replyRepository;

    /**
     * 검색 조건에 해당하는 공지 게시글 목록을 조회
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 공지 게시글 목록
     */
    public List<BoardNoticeDTO> searchNoticeBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchNoticeBoards(searchParamsDTO);
    }

    /**
     * 검색 조건에 해당하는 공지 게시글의 개수를 조회
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 공지 게시글의 개수
     */
    public int countNoticeBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countNoticeBoards(searchConditionDTO);
    }

    /**
     * 알림 표시된 공지 게시글 목록을 조회
     *
     * @return 알림 표시된 게시글 목록
     */
    public List<BoardNoticeDTO> getMarkedNoticedBoards() {
        return boardRepository.getMarkedNoticedBoards();
    }

    /**
     * 알림 표시된 공지 게시글의 개수를 조회
     *
     * @return 알림 표시된 게시글의 개수
     */
    public int countMarkedNoticedBoards() {
        return boardRepository.countMarkedNoticedBoards();
    }

    /**
     * 공지사항의 상세 내용을 조회
     *
     * @param boardId 게시글 ID
     * @return 공지사항의 상세 내용
     */
    public BoardNoticeDTO getNoticeBoardDetail(int boardId) {
        boardRepository.updateNoticeBoardVisitCount(boardId);
        return boardRepository.getNoticeBoardDetail(boardId);
    }

    /**
     * 공지사항의 카테고리 목록을 조회
     *
     * @return 공지사항의 카테고리 목록
     */
    public List<CategoryDTO> getNoticeBoardCategories() {

        BoardCategory categoryParentCode = BoardCategory.NOTICE_BOARD;
        return boardRepository.getNoticeBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 최근 공지사항 게시글 목록을 조회합니다.
     *
     * @param num 조회할 개수
     * @return 최근 공지사항 게시글 목록
     */
    public List<BoardNoticeDTO> getRecentNoticeBoards(int num) {
        return boardRepository.getRecentNoticeBoards(num);
    }

    /**
     * 자유 게시글을 검색하는 메서드
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 검색된 자유 게시글 목록
     */
    public List<BoardFreeDTO> searchFreeBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchFreeBoards(searchParamsDTO);
    }

    /**
     * 자유 게시글을 검색 조건에 맞춰 개수를 조회하는 메서드
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 검색된 자유 게시글의 개수
     */
    public int countFreeBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countFreeBoards(searchConditionDTO);
    }

    /**
     * 자유 게시글의 상세 내용을 조회하는 메서드
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
     * 자유게시판 카테고리 목록을 조회
     *
     * @return 자유게시판 카테고리 목록
     */
    public List<CategoryDTO> getFreeBoardCategories() {

        BoardCategory categoryParentCode = BoardCategory.FREE_BOARD;
        return boardRepository.getFreeBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 자유 게시글 정보를 저장하는 메서드
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
     * 자유 게시글 정보를 수정하는 메서드
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
     * 자유 게시글 수정 권한이 있는지 확인하는 메서드
     *
     * @param seqId   사용자 식별 ID
     * @param boardId 게시글 ID
     * @return 수정 권한 여부 (1: 있음, 0: 없음)
     */
    public int hasFreeBoardEditPermission(int seqId, int boardId) {
        return boardRepository.hasFreeBoardEditPermission(seqId, boardId);
    }

    /**
     * 자유 게시글을 삭제하는 메서드
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
     * 최근 자유 게시판 게시글 목록을 조회합니다.
     *
     * @param num 조회할 개수
     * @return 최근 자유 게시판 게시글 목록
     */
    public List<BoardFreeDTO> getRecentFreeBoards(int num) {
        return boardRepository.getRecentFreeBoards(num);
    }

    /**
     * 갤러리게시판 카테고리 목록을 조회합니다.
     *
     * @return 갤러리게시판 카테고리 목록
     */
    public List<CategoryDTO> getGalleryBoardCategories() {

        BoardCategory categoryParentCode = BoardCategory.GALLERY_BOARD;
        return boardRepository.getGalleryBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 갤러리게시판 정보를 저장합니다.
     *
     * @param seqId    사용자 식별자 아이디
     * @param boardDTO 갤러리게시판 정보 DTO
     * @throws Exception 예외 발생 시 처리
     */
    public void saveGalleryBoardInfo(int seqId, BoardGalleryDTO boardDTO) throws Exception {

        if (seqId <= 0) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

        boardRepository.saveGalleryBoardInfo(boardDTO);
        List<MultipartFile> newFiles = boardDTO.getUploadImages();
        int priority = 1;
        int boardId = boardDTO.getBoardId();
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {

                    String originName = file.getOriginalFilename();
                    File numberedFile = FileUtil.uploadImage(file, UPLOAD_PATH);

                    ImageDTO imagesDTO = ImageDTO.builder()
                            .boardId(boardId)
                            .fileName(numberedFile.getName())
                            .originFileName(originName)
                            .priority(priority)
                            .build();
                    imageRepository.saveImage(imagesDTO);

                    priority++;
                }
            }
        }

        ImageDTO firstPriorityImage = imageRepository.getFirstPriorityImageByBoardId(boardId);

        if (firstPriorityImage != null) {
            File newThumbNail = new File(UPLOAD_PATH + File.separator + firstPriorityImage.getFileName());
            Thumbnails.of(newThumbNail)
                    .height(200)
                    .keepAspectRatio(true)
                    .toFiles(new File(THUMBNAIL_PATH), Rename.NO_CHANGE);
        }

    }

    /**
     * 갤러리 게시판 정보를 수정합니다.
     *
     * @param seqId    사용자 식별자 아이디
     * @param boardDTO 갤러리 게시판 정보 DTO
     * @throws Exception 예외 발생 시 처리
     */
    public void updateGalleryBoardInfo(int seqId, BoardGalleryDTO boardDTO) throws Exception {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getGalleryBoardDetail(boardDTO.getBoardId()).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "수정 권한이 없습니다.");
        }
        //게시글 수정
        boardRepository.updateGalleryBoardInfo(boardDTO);

        //첨부파일 수정
        List<Integer> deletedAttachmentIds = boardDTO.getDeletedAttachmentIDs();
        int boardId = boardDTO.getBoardId();
        if (deletedAttachmentIds != null) {
            for (Integer deletedId : deletedAttachmentIds) {
                String deletedImageName = imageRepository.
                        getImageByImageId(deletedId).getFileName();

                //업로드 폴더/썸네일에서 이미지 삭제
                if (deletedImageName != null) {
                    File file = new File(UPLOAD_PATH + '/' + deletedImageName);
                    if (file.exists()) {
                        file.delete();
                    }
                    File thumbnail = new File(THUMBNAIL_PATH + '/' + deletedImageName);
                    if (thumbnail.exists()) {
                        file.delete();
                    }
                }
                //데이터베이스에서 이미지 정보 삭제
                imageRepository.deleteImageByImageId(deletedId);
            }
        }

        // 기존 이미지에 대해 우선순위 조정
        int lastPriority = 1;
        List<ImageDTO> remainingImages = imageRepository.getImagesByBoardId(boardId);

        if (!remainingImages.isEmpty()) {
            int newPriority = 1;
            for (ImageDTO image : remainingImages) {
                image.setPriority(newPriority);
                imageRepository.updateImagePriority(image);
                newPriority++;
            }
            lastPriority = newPriority;
        }

        //기존 이미지 -> 업로드 이미지 순으로 우선순위 적용
        List<MultipartFile> newFiles = boardDTO.getUploadImages();
        if (newFiles != null) {
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    String originName = file.getOriginalFilename();
                    File numberedFile = FileUtil.uploadImage(file, UPLOAD_PATH);

                    ImageDTO imagesDTO = ImageDTO.builder()
                            .boardId(boardDTO.getBoardId())
                            .fileName(numberedFile.getName())
                            .originFileName(originName)
                            .priority(lastPriority)
                            .build();
                    imageRepository.saveImage(imagesDTO);
                    lastPriority++;
                }
            }
        }
        //우선순위가 가장 높은 이미지를 썸네일로 사용
        ImageDTO firstPriorityImage = imageRepository.getFirstPriorityImageByBoardId(boardId);
        File newThumbNail = new File(UPLOAD_PATH + File.separator + firstPriorityImage.getFileName());
        if (firstPriorityImage != null) {
            Thumbnails.of(newThumbNail)
                    .height(200)
                    .keepAspectRatio(true)
                    .toFiles(new File(THUMBNAIL_PATH), Rename.NO_CHANGE);
        }
    }

    /**
     * 검색 조건에 해당하는 갤러리 게시글 목록을 조회합니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 검색 결과 갤러리 게시글 목록
     */
    public List<BoardGalleryDTO> searchGalleryBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchGalleryBoards(searchParamsDTO);
    }

    /**
     * 검색 조건에 해당하는 갤러리 게시글의 개수를 조회합니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 갤러리 게시글의 개수
     */
    public int countGalleryBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.countGalleryBoards(searchParamsDTO);
    }

    /**
     * 갤러리게시판의 상세 내용을 조회합니다.
     *
     * @param boardId 게시글 ID
     * @return 갤러리게시판 상세 내용
     */
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

    /**
     * 갤러리게시판 수정 권한 여부를 확인합니다.
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @return 권한 여부 (1: 권한 있음, 0: 권한 없음)
     */
    public int hasGalleryBoardEditPermission(int seqId, int boardId) {
        return boardRepository.hasGalleryBoardEditPermission(seqId, boardId);
    }

    /**
     * 갤러리게시판을 삭제합니다.
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     */
    public void deleteGalleryBoard(int seqId, int boardId) {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getGalleryBoardDetail(boardId).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }
        imageRepository.deleteImageByBoardId(boardId);
        boardRepository.deleteGalleryBoard(boardId);
    }

    /**
     * 최근 갤러리 게시판 게시글 목록을 조회합니다.
     *
     * @param num 조회할 개수
     * @return 최근 갤러리 게시판 게시글 목록
     */
    public List<BoardGalleryDTO> getRecentGalleryBoards(int num) {
        return boardRepository.getRecentGalleryBoards(num);
    }

    /**
     * 문의게시판 카테고리 목록을 조회합니다.
     *
     * @return 문의게시판 카테고리 목록
     */
    public List<CategoryDTO> getInquiryBoardCategories() {
        BoardCategory categoryParentCode = BoardCategory.INQUIRY_BOARD;
        return boardRepository.getInquiryBoardCategories(categoryParentCode.getCategoryParentCodeValue());
    }

    /**
     * 검색 조건에 해당하는 문의 게시글 목록을 조회합니다.
     *
     * @param searchParamsDTO 검색 조건 DTO
     * @return 검색 결과 문의 게시글 목록
     */
    public List<BoardInquiryDTO> searchInquiryBoards(SearchConditionDTO searchParamsDTO) {
        return boardRepository.searchInquiryBoards(searchParamsDTO);
    }

    /**
     * 검색 조건에 해당하는 문의 게시글의 개수를 조회합니다.
     *
     * @param searchConditionDTO 검색 조건 DTO
     * @return 문의 게시글의 개수
     */
    public int countInquiryBoards(SearchConditionDTO searchConditionDTO) {
        return boardRepository.countInquiryBoards(searchConditionDTO);
    }

    /**
     * 문의 게시글 정보를 저장합니다.
     *
     * @param seqId    사용자 식별자 아이디
     * @param boardDTO 문의 게시글 정보 DTO
     * @throws Exception 예외 발생 시 처리
     */
    public void saveInquiryBoardInfo(int seqId, BoardInquiryDTO boardDTO) throws Exception {

        if (seqId <= 0) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }

        if (boardDTO.getIsSecret() == 1 && boardDTO.getPassword().length() < 4) {
            throw new AppException(ErrorCode.BAD_REQUEST, "게시글 비밀번호는 4자 이상입니다.");
        }
        boardRepository.saveInquiryBoardInfo(boardDTO);

    }

    /**
     * 문의 게시판 수정 권한 여부를 확인합니다.
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @return 권한 여부 (1: 권한 있음, 0: 권한 없음)
     */
    public int hasInquiryBoardEditPermission(int seqId, int boardId) {
        return boardRepository.hasInquiryBoardEditPermission(seqId, boardId);
    }

    /**
     * 문의 게시판의 상세 내용을 조회하고, 관련된 댓글을 포함하여 반환합니다.
     *
     * @param boardId 게시글 ID
     * @return 문의 게시판 상세 내용
     */
    public BoardInquiryDTO getInquiryBoardDetail(int boardId) {
        BoardInquiryDTO boardDTO = boardRepository.getInquiryBoardDetail(boardId);
        if (ObjectUtils.isEmpty(boardDTO)) {
            return null;
        }
        boardRepository.updateInquiryBoardVisitCount(boardId);

        List<ReplyDTO> replies = replyRepository.getRepliesByBoardId(boardId);
        boardDTO.setBoardReplies(replies);

        return boardDTO;
    }

    /**
     * 문의 게시판을 삭제합니다.
     *
     * @param seqId   사용자 식별자 아이디
     * @param boardId 게시글 ID
     * @throws AppException 삭제 권한이 없거나 남아있는 답변이 있을 경우 발생하는 예외
     */
    public void deleteInquiryBoard(int seqId, int boardId) {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교

        int getUserSeqId = boardRepository.getInquiryBoardDetail(boardId).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "삭제 권한이 없습니다.");
        }

        if (replyRepository.countRepliesByBoardId(boardId) > 0) {
            throw new AppException(ErrorCode.REMAIN_REPLY, "답변 남아있어서 게시글 삭제가 불가합니다.");
        }
        boardRepository.deleteInquiryBoard(boardId);
    }

    /**
     * 문의 게시판의 비밀번호를 확인합니다.
     *
     * @param boardId  게시글 ID
     * @param boardDTO 게시판 정보 DTO
     * @return 비밀번호 일치 여부
     * @throws AppException 비밀번호가 틀렸을 경우 발생하는 예외
     */
    public boolean checkInquiryBoardPassword(int boardId, BoardInquiryDTO boardDTO) {
        BoardInquiryDTO boardInfo = boardRepository.getInquiryBoardDetail(boardId);

        if (!boardInfo.getPassword().equals(boardDTO.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "비밀번호가 틀렸습니다.");
        }
        return true;
    }

    /**
     * 문의 게시판 정보를 수정합니다.
     *
     * @param seqId    사용자 식별자 아이디
     * @param boardDTO 문의 게시판 정보 DTO
     * @throws Exception 예외 발생 시 처리
     */
    public void updateInquiryBoardInfo(int seqId, BoardInquiryDTO boardDTO) throws Exception {
        //현재 userSeqId와 게시글 정보에 저장된 userSeqId와 비교
        int getUserSeqId = boardRepository.getInquiryBoardDetail(boardDTO.getBoardId()).getUserSeqId();
        if (seqId != getUserSeqId) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "수정 권한이 없습니다.");
        }
        boardRepository.updateInquiryBoardInfo(boardDTO);
    }

    /**
     * 최근 문의 게시판 게시글 목록을 조회합니다.
     *
     * @param num 조회할 개수
     * @return 최근 문의 게시판 게시글 목록
     */
    public List<BoardInquiryDTO> getRecentInquiryBoards(int num) {
        return boardRepository.getRecentInquiryBoards(num);
    }

}
