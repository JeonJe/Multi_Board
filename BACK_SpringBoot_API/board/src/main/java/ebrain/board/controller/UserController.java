package ebrain.board.controller;

import ebrain.board.dto.UserLoginDTO;
import ebrain.board.dto.UserSignupDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.response.APIResponse;
import ebrain.board.response.UserLoginResponse;
import ebrain.board.service.UserService;
import ebrain.board.utils.ResponseUtil;

import ebrain.board.vo.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * UserController class
 * 유저 관련 요청을 처리하고 APIResponse 데이터 반환합니다.
 */
@Validated

@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<APIResponse> signupUser(@Valid @RequestBody UserSignupDTO userSignupDTO) {


        //회원가입 성공 후 JWT 토큰발행
        String jwtToken = userService.createJwtToken(userSignupDTO.getUserId());
        UserLoginResponse userLoginInfo = UserLoginResponse.builder()
                .userId(userSignupDTO.getUserId())
                .name(userSignupDTO.getName())
                .jwt(jwtToken)
                .build();

        APIResponse apiResponse = ResponseUtil.SuccessWithData("회원가입에 성공하였습니다", userLoginInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 특정 사용자 ID가 중복되는지 확인합니다.
     *
     * @param userId 사용자 ID
     * @return 중복 여부에 따른 API 응답 객체 (ResponseEntity<APIResponse>)
     * @throws AppException 중복된 ID일 경우 발생하는 예외 (ErrorCode.DUPLICATE_USERID)
     */
    @GetMapping("/api/auth/check/{userId}")
    public ResponseEntity<APIResponse>
    checkDuplicateId(@PathVariable
                     @NotEmpty(message = "ID는 필수 항목입니다")
                     @Size(min = 4, max = 11, message = "ID는 4자 이상 11자 이하로 입력해야 합니다")
                     @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "ID는 영문자, 숫자, '-', '_'만 사용할 수 있습니다") String userId) {

        User user = userService.findUserByUserId(userId);
        if (!ObjectUtils.isEmpty(user)) {
            throw new AppException(ErrorCode.DUPLICATE_USERID, user.getUserId() + "는 이미 가입된 아이디입니다.");
        }

        APIResponse apiResponse = ResponseUtil.SuccessWithoutData("사용할 수 있는 아이디입니다");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param userLoginDTO 사용자 로그인 정보
     * @return ResponseEntity<APIResponse> 아이디 패스워드 확인에 따른 API 응답
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<APIResponse> loginUser(@RequestBody UserLoginDTO userLoginDTO) {

        //아이디, 비밀번호 확인
        boolean isValidUser = (userService.checkUserCredentials(userLoginDTO) == 1)  ? true : false;

        if (isValidUser) {
            String userId = userLoginDTO.getUserId();
            String name = userService.findUserByUserId(userId).getName();
            //JWT 토큰 생성
            String jwtToken = userService.createJwtToken(userLoginDTO.getUserId());
            //아이디, 네임, jwt 토큰과 함꼐 전달

            UserLoginResponse userLoginInfo = UserLoginResponse.builder()
                    .userId(userId)
                    .name(name)
                    .jwt(jwtToken)
                    .build();

            APIResponse apiResponse = ResponseUtil.SuccessWithData("로그인 성공", userLoginInfo);
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } else {
            throw new AppException(ErrorCode.INVALID_AUTH_USER, "아이디, 비밀번호가 틀렸습니다.");
        }
    }

    /**
     * 클라이언트의 JWT 토큰을 추출하고 검증하여 userId를 얻어온 후, 해당 userId를 사용하여 사용자를 조회합니다.
     *
     * @param request HttpServletRequest 객체 (요청 객체)
     * @return APIResponse 객체에 담긴 사용자 정보와 함께 HTTP 응답 엔티티
     */
    @GetMapping("/api/auth/check")
    public ResponseEntity<APIResponse> checkUserToken(HttpServletRequest request) {

        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");
        User user = userService.findUserByUserId(userId);

        if (ObjectUtils.isEmpty(user)) {
            throw new AppException(ErrorCode.INVALID_AUTH_TOKEN, "유효한 사용자가 아닙니다.");
        }

        APIResponse apiResponse = ResponseUtil.SuccessWithData("유효한 사용자입니다.", user.getName());
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    /**
     * JWT 토큰 인증 상태를 확인합니다.
     *
     * @param request HttpServletRequest 객체
     * @return API 응답 객체
     */
    @GetMapping("/api/auth/status")
    public ResponseEntity<APIResponse> getAuthenticationStatus(HttpServletRequest request) {

        //BearerAuthInterceptor에서 JWT에 따른 userId를 포함한 Request를 전달
        String userId = (String) request.getAttribute("userId");

        User user = userService.findUserByUserId(userId);
        if (ObjectUtils.isEmpty(user)) {
            throw new AppException(ErrorCode.INVALID_AUTH_TOKEN, "유효한 사용자가 아닙니다.");
        }


        APIResponse apiResponse = ResponseUtil.SuccessWithData("유효한 토큰입니다.", true);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
