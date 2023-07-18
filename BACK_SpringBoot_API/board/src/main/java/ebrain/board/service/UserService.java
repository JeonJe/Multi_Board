package ebrain.board.service;

import ebrain.board.dto.UserLoginDTO;
import ebrain.board.dto.UserSignupDTO;
import ebrain.board.exception.AppException;
import ebrain.board.exception.ErrorCode;
import ebrain.board.mapper.UserRepository;
import ebrain.board.security.JwtTokenProvider;
import ebrain.board.utils.AuthUtil;
import ebrain.board.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserService 클래스
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 정보 저장 메서드
     *
     * @param userSignupDTO 사용자 회원가입 정보를 담은 UserSignupDTO 객체
     * @throws AppException 회원가입 과정에서 발생하는 예외
     */
    public void saveUser(UserSignupDTO userSignupDTO) {

        //아이디 중복 확인
        User user = userRepository.findUserByUserId(userSignupDTO.getUserId());
        if (user != null) {
            throw new AppException(ErrorCode.DUPLICATE_USERID, user.getUserId() + "는 이미 가입된 아이디입니다.");
        }

        String hashedPassword = AuthUtil.hashPassword(userSignupDTO.getPassword());
        User newUser = User.builder()
                .userId(userSignupDTO.getUserId())
                .password(hashedPassword)
                .name(userSignupDTO.getName())
                .build();

        userRepository.saveUser(newUser);

    }

    /**
     * 사용자 아이디로 사용자 정보 조회 메서드
     *
     * @param userId 사용자 아이디
     * @return 사용자 객체
     */
    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    /**
     * 주어진 사용자 ID를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 JWT 토큰
     */
    public String createJwtToken(String userId) {
        return jwtTokenProvider.createToken(userId);

    }
    /**
     * 주어진 사용자 로그인 정보를 기반으로 사용자 인증을 확인합니다.
     *
     * @param userLoginDTO 사용자 로그인 정보 DTO
     * @return 사용자 인증 결과 (1: 인증 성공, 0: 인증 실패)
     */
    public int checkUserCredentials(UserLoginDTO userLoginDTO) {
        String hashedPassword = AuthUtil.hashPassword(userLoginDTO.getPassword());
        return userRepository.checkUserCredentials(userLoginDTO.getUserId(), hashedPassword);
    }


}
