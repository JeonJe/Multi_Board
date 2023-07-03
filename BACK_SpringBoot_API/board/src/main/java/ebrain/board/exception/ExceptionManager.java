package ebrain.board.exception;

import ebrain.board.response.APIResponse;
import ebrain.board.utils.ResponseUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        APIResponse apiResponse = ResponseUtil.createErrorWithData("잘못된 요청입니다", errorMessages);

        return ResponseEntity.badRequest().body(apiResponse);
    }
    /**
     * ConstraintViolationException이 발생했을 때 호출되어 유효성 검사 실패 메시지를 반환합니다.
     *
     * @param e ConstraintViolationException 예외 객체
     * @return APIResponse 객체
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIResponse> handleConstraintViolationException(ConstraintViolationException e) {
        // Handle constraint violation exception and return a custom response
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        List<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        APIResponse apiResponse = ResponseUtil.createErrorWithData("잘못된 요청입니다.", errorMessages);

        return ResponseEntity.badRequest().body(apiResponse);
    }
    /**
     * AppException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e AppException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> appExceptionHandler(AppException e){

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(e.getMessage());
    }
    /**
     * RuntimeException이 발생했을 때 호출되어 예외 메시지를 반환합니다.
     *
     * @param e RuntimeException 예외 객체
     * @return ResponseEntity 객체
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
}
