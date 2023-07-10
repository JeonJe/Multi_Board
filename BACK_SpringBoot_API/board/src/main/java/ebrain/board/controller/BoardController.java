package ebrain.board.controller;

import ebrain.board.response.BoardSearchResponse;
import ebrain.board.vo.NoticeBoard;
import ebrain.board.dto.SearchConditionDTO;
import ebrain.board.response.APIResponse;
import ebrain.board.service.BoardService;
import ebrain.board.utils.ResponseUtil;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
     * 검색 조건에 해당하는 공지 게시글 목록을 가져옵니다.
     *
     * @param searchCondition 검색 조건 객체
     * @return API 응답 객체
     */
    @GetMapping("/api/boards/notice")
    ResponseEntity<APIResponse> getNoticeBoardsWitchSearchCondition(@ModelAttribute SearchConditionDTO searchCondition) {
        List<NoticeBoard> searchResult = boardService.searchNoticeBoards(searchCondition);
        int countNoticeBoards = boardService.countNoticeBoards(searchCondition);

        List<NoticeBoard> markedNoticedBoards = boardService.getMarkedNoticedBoards();
        int countMarkedNoticedBoards = boardService.countMarkedNoticedBoards();
        //TODO : 카테고리는?

        BoardSearchResponse boardSearchResponse = BoardSearchResponse
                .builder()
                .searchNoticeBoards(searchResult)
                .countNoticeBoards(countNoticeBoards)
                .markNoticedBoards(markedNoticedBoards)
                .countMarkNoticedBoards(countMarkedNoticedBoards)
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
        NoticeBoard noticeBoard = boardService.getNoticeBoardDetail(boardId);

        APIResponse apiResponse = ResponseUtil.SuccessWithData("공지사항 상세 내용입니다.", noticeBoard);
        if (ObjectUtils.isEmpty(noticeBoard)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(apiResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }
    }


}
