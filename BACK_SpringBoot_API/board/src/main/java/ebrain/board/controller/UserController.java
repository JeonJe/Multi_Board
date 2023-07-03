package ebrain.board.controller;

import ebrain.board.dto.UserSignupDTO;
import ebrain.board.response.APIResponse;
import ebrain.board.service.UserService;
import ebrain.board.utils.ResponseUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * UserController class
 * 유저 관련 요청을 처리하고 APIResponse 데이터 반환합니다.
 */
@CrossOrigin(origins = "http://localhost:8082")
@RequiredArgsConstructor
@RestController
public class UserController {

    /**
     * 유저 서비스
     */
    private final UserService userService;

    /**
     * 사용자 회원 가입
     *
     * @param userSignupDTO 사용자 회원 가입 정보를 담고 있는 DTO입니다.
     * @return 회원 가입 처리 결과를 담은 APIResponse를 포함하는 ResponseEntity입니다.
     */
    @PostMapping("/api/auth/signup")
    public ResponseEntity<APIResponse> signupUser(@Valid @RequestBody UserSignupDTO userSignupDTO){

        userService.saveUser(userSignupDTO);

        //TODO : 회원가입 성공 후 JWT 토큰발행
        //TODO : SQL Exception 처리 추가

        APIResponse apiResponse = ResponseUtil.createSuccessWithoutData("회원가입에 성공하였습니다");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }


}
