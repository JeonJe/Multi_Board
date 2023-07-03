package ebrain.board.controller;

import ebrain.board.dto.UserLoginDTO;
import ebrain.board.dto.UserSignupDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.response.APIResponse;
import ebrain.board.service.UserService;
import ebrain.board.utils.ResponseUtil;

import ebrain.board.vo.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * UserController class
 * 유저 관련 요청을 처리하고 APIResponse 데이터 반환합니다.
 */

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:8082")
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

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("회원가입에 성공하였습니다");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/api/auth/check/{userId}")
    public ResponseEntity<APIResponse> checkDuplicateId(@PathVariable String userId){

        User user = userService.findUserByUserId(userId);
        if (user != null){
            throw new AppException(ErrorCode.DUPLICATE_USERID, user.getUserId() + "는 이미 가입된 아이디입니다.");
        }

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("사용할 수 있는 아이디입니다");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<APIResponse> loginUser(@RequestBody UserLoginDTO userLoginDTO){
        String userId = userLoginDTO.getUserId();
        String password = userLoginDTO.getPassword();

        //아이디, 비밀번호 확인

        //토큰 생성, 전달
        String jwtToken = userService.createJwtToken(userLoginDTO);
        APIResponse apiResponse = ResponseUtil.SuccessWithData("로그인 성공", jwtToken);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

    }




}
