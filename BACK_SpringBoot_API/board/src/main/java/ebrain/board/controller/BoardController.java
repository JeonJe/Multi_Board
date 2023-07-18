package ebrain.board.controller;

import ebrain.board.dto.*;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.response.BoardSearchResponse;
import ebrain.board.response.APIResponse;
import ebrain.board.service.AttachmentService;
import ebrain.board.service.BoardService;
import ebrain.board.service.CommentService;
import ebrain.board.utils.FileUtil;
import ebrain.board.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class BoardController {

    /**
     * 유저 서비스
     */
    private final BoardService boardService;

    /**
     * 첨부파일 서비스
     */
    private final AttachmentService attachmentService;

    /**
     * 첨부파일 서비스
     */
    private final CommentService commentService;


    /**
     * 파일 업로드 경로
     */
    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;


    /**
     * 검색 조건에 해당하는 공지 게시글 목록을 가져옵니다.
     *
     * @param searchCondition 검색 조건 객체
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/notice")
    ResponseEntity<APIResponse> getNoticeBoardsWitchSearchCondition(@ModelAttribute SearchConditionDTO searchCondition) {
        List<BoardDTO> searchResult = boardService.searchNoticeBoards(searchCondition);
        int countNoticeBoards = boardService.countNoticeBoards(searchCondition);

        List<BoardDTO> markedNoticedBoards = boardService.getMarkedNoticedBoards();
        int countMarkedNoticedBoards = boardService.countMarkedNoticedBoards();

        BoardSearchResponse boardSearchResponse = BoardSearchResponse
                .builder()
                .searchBoards(searchResult)
                .countSearchBoards(countNoticeBoards)
                .markNoticedBoards(markedNoticedBoards)
                .build();

        APIResponse apiResponse = ResponseUtil.SuccessWithData("검색조건에 해당하는 공지 게시글 목록입니다.", boardSearchResponse);

        if (countNoticeBoards == 0 && countMarkedNoticedBoards == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 알림 표시된 게시글 목록 개수를 가져옵니다.
     *
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/notice/cnt/noticed")
    ResponseEntity<APIResponse> getMarkNoticedBoards() {
        int countMarkedNoticedBoards = boardService.countMarkedNoticedBoards();
        APIResponse apiResponse = ResponseUtil.SuccessWithData("알림 표시된 게시글 목록 개수입니다.", countMarkedNoticedBoards);
        if (countMarkedNoticedBoards == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 공지사항의 상세 내용을 가져옵니다.
     *
     * @param boardId 게시글 ID
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/notice/{boardId}")
    ResponseEntity<APIResponse> getNoticeBoardDetail(@PathVariable @NotEmpty int boardId) {
        BoardDTO noticeBoard = boardService.getNoticeBoardDetail(boardId);

        APIResponse apiResponse = ResponseUtil.SuccessWithData("공지사항 상세 내용입니다.", noticeBoard);
        if (ObjectUtils.isEmpty(noticeBoard)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 공지 게시판의 카테고리 목록을 가져옵니다.
     *
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/notice/categories")
    ResponseEntity<APIResponse> getNoticeBoardCategories() {
        List<CategoryDTO> categories = boardService.getNoticeBoardCategories();

        APIResponse apiResponse = ResponseUtil.SuccessWithData("공지사항 카테고리 목록입니다.", categories);
        if (ObjectUtils.isEmpty(categories)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 검색 조건에 해당하는 자유 게시글 목록을 가져옵니다.
     *
     * @param searchCondition 검색 조건 객체
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/free")
    ResponseEntity<APIResponse> getFreeBoardsWitchSearchCondition(@ModelAttribute SearchConditionDTO searchCondition) {
        List<BoardDTO> searchResult = boardService.searchFreeBoards(searchCondition);
        int countFreeBoards = boardService.countFreeBoards(searchCondition);

        BoardSearchResponse boardSearchResponse = BoardSearchResponse
                .builder()
                .searchBoards(searchResult)
                .countSearchBoards(countFreeBoards)
                .build();

        APIResponse apiResponse = ResponseUtil.SuccessWithData("검색조건에 해당하는 자유 게시글 목록입니다.", boardSearchResponse);

        if (countFreeBoards == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 자유게시글의 상세 내용을 가져옵니다.
     *
     * @param boardId 게시글 ID
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/free/{boardId}")
    ResponseEntity<APIResponse> getFreeBoardDetail(@PathVariable @NotEmpty int boardId) {
        BoardDTO noticeBoard = boardService.getFreeBoardDetail(boardId);

        APIResponse apiResponse = ResponseUtil.SuccessWithData("자유게시글 상세 내용입니다.", noticeBoard);
        if (ObjectUtils.isEmpty(noticeBoard)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }

    /**
     * 자유 게시판의 카테고리 목록을 가져옵니다.
     *
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/free/categories")
    ResponseEntity<APIResponse> getFreeBoardCategories() {
        List<CategoryDTO> categories = boardService.getFreeBoardCategories();

        APIResponse apiResponse = ResponseUtil.SuccessWithData("자유게시판 카테고리 목록입니다.", categories);
        if (ObjectUtils.isEmpty(categories)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }
    /**
     * 자유 게시글을 저장합니다.
     *
     * @param request   HttpServletRequest 객체
     * @param boardDTO  저장할 게시글 정보
     * @return          API 응답 객체
     * @throws Exception 예외 발생 시
     */
    @PostMapping("/api/boards/free")
    ResponseEntity<APIResponse> saveFreeBoardInfo(HttpServletRequest request, @Valid @ModelAttribute BoardDTO boardDTO) throws Exception {

        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");

        if (StringUtils.isEmpty(userId) || !userId.equals(boardDTO.getUserId())) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, "유효한 사용자가 아닙니다.");
        }
        boardService.saveFreeBoardInfo(boardDTO);

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("게시글 저장에 성공하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    /**
     * 첨부 파일을 다운로드합니다.
     *
     * @param attachmentId   다운로드할 첨부 파일의 ID
     * @return               Resource 객체를 ResponseEntity로 래핑한 결과
     * @throws Exception     예외 발생 시
     */
    @GetMapping("api/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable @NotEmpty int attachmentId)
            throws Exception{

            AttachmentDTO attachment = attachmentService.getAttachmentByAttachmentId(attachmentId);
            return FileUtil.fileDownload(attachment, UPLOAD_PATH);
    }

    @GetMapping("/api/auth/boards/free/{boardId}")
    public ResponseEntity<APIResponse> hasFreeBoardEditPermission(HttpServletRequest request, @PathVariable int boardId) {

        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");

        //boardId 작성자와 userId가 동일하면 true
        boolean hasPermission = (boardService.hasFreeBoardEditPermission(userId, boardId) == 1) ? true : false;

        if (hasPermission) {
            APIResponse apiResponse = ResponseUtil.SuccessWithData("게시글 작성자와 동일합니다.", true);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } else{
            APIResponse apiResponse = ResponseUtil.SuccessWithData("게시글 작성자와 동일하지 않습니다.", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
    }

    @DeleteMapping("/api/boards/free/{boardId}")
    public ResponseEntity<APIResponse> deleteFreeBoard(HttpServletRequest request, @PathVariable int boardId) {
        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");

        int countBoardComment = commentService.countCommentByFreeBoardId(boardId);
        if (countBoardComment > 0){
            APIResponse apiResponse = ResponseUtil.ErrorWithoutData("댓글이 남아있어서 삭제가 불가합니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
        attachmentService.deleteAttachmentsByBoardId(boardId);
        boardService.deleteFreeBoard(userId, boardId);

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("게시글 삭제에 성공하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/api/boards/free/{boardId}/comments")
    public ResponseEntity<APIResponse> addFreeBoardComment(HttpServletRequest request,@PathVariable int boardId, @RequestBody CommentDTO commentDTO) {

        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");

        CommentDTO newComment = CommentDTO.builder()
                .boardId(boardId)
                .content(commentDTO.getContent())
                .userId(userId)
                .build();

        commentService.addFreeBoardComment(newComment);

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("댓글 추가에 성공하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    

}
