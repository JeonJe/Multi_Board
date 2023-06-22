package ebrain.board.controller;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;

/**
 * BoardController class
 * 게시판과 관련된 모든 요청을 처리하고 APIResponse 데이터 반환
 */
@CrossOrigin(origins = "http://localhost:8082")
@RestController

public class BoardController {


    /**
     * 파일 업로드 경로
     */
    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;





}
