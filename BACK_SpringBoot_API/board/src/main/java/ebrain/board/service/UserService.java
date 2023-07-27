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
     * 사용자 정보를 저장하는 메서드입니다.
     *
     * @param userSignupDTO 사용자 회원가입 정보를 담은 UserSignupDTO 객체
     * @return 저장된 사용자의 식별 ID
     * @throws AppException 회원가입 과정에서 발생하는 예외
     */
    public int saveUser(UserSignupDTO userSignupDTO) {

        //아이디 중복 확인
        User findUserIdInUser = userRepository.findUserByUserIdInUser(userSignupDTO.getUserId());
        User findUserIdInAdmin = userRepository.findUserByUserIdInAdmin(userSignupDTO.getUserId());
        if (findUserIdInUser != null || findUserIdInAdmin != null) {
            throw new AppException(ErrorCode.DUPLICATE_USERID, findUserIdInUser.getUserId() + "는 사용중인 아이디입니다.");
        }

        String hashedPassword = AuthUtil.hashPassword(userSignupDTO.getPassword());
        User newUser = User.builder()
                .userId(userSignupDTO.getUserId())
                .password(hashedPassword)
                .name(userSignupDTO.getName())
                .build();

        userRepository.saveUser(newUser);
        return newUser.getSeqId();
    }

    /**
     * 주어진 사용자 아이디로 사용자 정보를 조회하는 메서드입니다.
     *
     * @param userId 사용자 아이디
     * @return 조회된 사용자 객체
     */
    public User findUserByUserIdInUser(String userId) {
        return userRepository.findUserByUserIdInUser(userId);
    }
    /**
     * 주어진 사용자 식별 ID를 기반으로 사용자 정보를 조회하는 메서드입니다.
     *
     * @param seqId 사용자 식별 ID
     * @return 조회된 사용자 객체
     */
    public User findUserBySeqId(int seqId) {
        return userRepository.findUserBySeqIdInUser(seqId);
    }


    /**
     * 주어진 사용자 식별 ID를 기반으로 JWT 토큰을 생성하는 메서드입니다.
     *
     * @param seqId 사용자 식별 ID
     * @return 생성된 JWT 토큰
     */
    public String createJwtToken(int seqId) {
        return jwtTokenProvider.createToken(seqId);

    }
    /**
     * 주어진 사용자 로그인 정보를 기반으로 사용자 인증을 확인하는 메서드입니다.
     *
     * @param userLoginDTO 사용자 로그인 정보 DTO
     * @return 사용자 인증 결과 (1: 인증 성공, 0: 인증 실패)
     */
    public int checkUserCredentials(UserLoginDTO userLoginDTO) {
        String hashedPassword = AuthUtil.hashPassword(userLoginDTO.getPassword());
        return userRepository.checkUserCredentials(userLoginDTO.getUserId(), hashedPassword);
    }


}
