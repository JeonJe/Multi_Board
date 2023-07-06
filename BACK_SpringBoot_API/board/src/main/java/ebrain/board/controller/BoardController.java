package ebrain.board.controller;

import ebrain.board.vo.NoticeBoard;
import ebrain.board.dto.SearchConditionDTO;
import ebrain.board.response.APIResponse;
import ebrain.board.service.BoardService;
import ebrain.board.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8082")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/boards/notice")
    ResponseEntity<APIResponse> getNoticeBoardListWitchSearchCondition(@ModelAttribute SearchConditionDTO searchCondition){
        List<NoticeBoard> searchResult = boardService.searchNoticeBoards(searchCondition);
        //TODO : 빈리스트일때 상태코드 확인
        APIResponse apiResponse = ResponseUtil.SuccessWithData("리스트 반환", searchResult);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        //TODO : total Cnt 개수도 같이 반환 어떻게 할 것인지?
        //TODO : 카테고리는?

    }

}
