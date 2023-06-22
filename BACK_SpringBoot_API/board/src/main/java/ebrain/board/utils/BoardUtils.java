package ebrain.board.utils;

import ebrain.board.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
/**
 * 첨부 파일을 다운로드하거나 API 응답을 생성하는 게시판 관련 유틸리티 클래스입니다.
 */
public class BoardUtils {


    /**
     * 폼 유효성을 검사하는 메서드
     *
     * @param writer         작성자
     * @param password       비밀번호
     * @param passwordConfirm 비밀번호 확인
     * @param title          제목
     * @param content        내용
     * @return 폼 유효성 검사 결과
     */
    public static boolean checkFormValidation(String writer, String password, String passwordConfirm, String title, String content) {
        try {

            if (writer.length() < 3 || writer.length() >= 5) {
                return false;
            }
            if (password.length() < 4 || password.length() >= 16 || !password.equals(passwordConfirm)) {
                return false;
            }
            String passwordRegex = "(?i)^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
            if (!password.matches(passwordRegex)) {
                return false;
            }
            if (title.length() < 4 || title.length() >= 100) {
                return false;
            }
            if (content.length() < 4 || content.length() >= 2000) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }



    /**
     * 비밀번호를 해싱하는 메서드
     *
     * @param password 비밀번호
     * @return 해시화된 비밀번호
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashedPassword = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hashedPassword.append(String.format("%02x", hashByte));
            }
            return hashedPassword.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseEntity<APIResponse> createBadRequestResponse(String message) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(message);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    /**
     * 성공적인 API 응답을 생성합니다.
     *
     * @param data 응답 데이터
     * @return 성공적인 API 응답에 대한 ResponseEntity
     */
    public static ResponseEntity<APIResponse> createOkResponse(Object data) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(true);
        apiResponse.setMessage("Success");
        apiResponse.setData(data);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * 내부 서버 오류에 대한 API 응답을 생성합니다.
     *
     * @param errorMessage 오류 메시지
     * @return 내부 서버 오류에 대한 ResponseEntity
     */
    public static ResponseEntity<APIResponse> createInternalServerErrorResponse(String errorMessage) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setMessage(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}
