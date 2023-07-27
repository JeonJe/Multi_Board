package ebrain.board.exception;

import ebrain.board.response.APIResponse;
import ebrain.board.utils.ResponseBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ExceptionManager 클래스
 * 전역 예외 처리를 담당하는 @RestControllerAdvice 클래스입니다.
 */
@RestControllerAdvice
public class ExceptionManager {

    /**
     * MethodArgumentNotValidException이 발생했을 때 호출되어 유효성 검사 실패 메시지를 반환합니다.
     *
     * @param e MethodArgumentNotValidException 예외 객체
     * @return APIResponse 객체
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errorMessages = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        APIResponse apiResponse = ResponseBuilder.ErrorWithData("잘못된 요청입니다", errorMessages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * ConstraintViolationException이 발생했을 때 호출되어 유효성 검사 실패 메시지를 반환합니다.
     *
     * @param e ConstraintViolationException 예외 객체
     * @return APIResponse 객체
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse> handleConstraintViolationException(ConstraintViolationException e) {

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());


        APIResponse apiResponse = ResponseBuilder.ErrorWithData("잘못된 요청입니다.", errorMessages.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<APIResponse> handleSQLException(SQLException e) {
        String errorMessage = "서버 오류가 발생하였습니다.(SQL Exception)";

        APIResponse apiResponse = ResponseBuilder.ErrorWithoutData(errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    /**
     * AppException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e AppException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<APIResponse> handleAppException(AppException e) {
        APIResponse apiResponse = ResponseBuilder.ErrorWithData("에러가 발생하였습니다", e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgumentException(IllegalArgumentException e) {

        APIResponse apiResponse = ResponseBuilder.ErrorWithData("유효하지 않는 요청입니다.", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * RuntimeException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e RuntimeException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    /**
     * IOException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e IOException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException e) {
        // IOException에 대한 처리 로직
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    /**
     * MultipartException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e MultipartException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException e) {
        // MultipartException에 대한 처리 로직
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

}
