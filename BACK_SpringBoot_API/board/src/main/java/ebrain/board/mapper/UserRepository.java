package ebrain.board.mapper;

import ebrain.board.vo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserRepository 인터페이스
 * 사용자 정보에 대한 데이터베이스 액세스를 담당하는 매퍼 인터페이스입니다.
 */
@Mapper
public interface UserRepository {

    /**
     * 사용자 정보 저장 메서드
     *
     * @param user 사용자 객체
    * @return 사용자 식별 ID
     */
    int saveUser(User user);


    User findUserByUserIdInUser(String userId);


    User findUserByUserIdInAdmin(String userId);

    User findUserBySeqIdInUser(int seqId);


    /**
     * 로그인 메서드
     *
     * @param userId 사용자 아이디
     * @param password 사용자 비밀번호
     * @return 사용자 아이디와 비밀번호와 일치하는 데이터 수
     */
    int checkUserCredentials(String userId, String password);



}
