package ebrain.board.utils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 인증 관련 유틸리티 클래스입니다.
 */
public class AuthUtil {


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
    /**
     * HttpServletRequest에서 "seqId" 속성값을 추출하여 사용자 식별 ID를 반환합니다.
     *
     * @param request HttpServletRequest 객체
     * @return 추출된 사용자 식별 ID (seqId) 또는 값이 없을 경우 0을 반환합니다.
     * @throws NumberFormatException seqId가 올바른 정수로 파싱되지 않을 경우 발생할 수 있는 예외
     */
    public static int getSeqIdFromRequest(HttpServletRequest request) {
        String seqIdString = (String) request.getAttribute("seqId");
        return StringUtils.isEmpty(seqIdString) ? 0 : Integer.parseInt(seqIdString);
    };

}
