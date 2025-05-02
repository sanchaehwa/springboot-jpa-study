package org.com.jwtshop.global.error;


import lombok.Getter;
import org.springframework.validation.BindingResult; //BindingResult : 검증에 실패한 필드와 오류 메시지를 담고 Custom Error 응답을 만들때 필요.

import java.util.*;

@Getter
public class ErrorResponse {

    private String message;
    private int status;
    private List<FieldError> errors;

    protected ErrorResponse() {
    }
    private ErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errors = new ArrayList<>();
    }
    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    @Getter
    public static class FieldError {
        private String field;
        private String value;
        private String reason;
        protected FieldError() {
        }
        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
        private static List<FieldError> of(final BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors
                    .stream()
                    .map(filedError -> new FieldError(
                            filedError.getField(),
                            filedError.getRejectedValue() == null ? "" : filedError
                                    .getRejectedValue()
                                    .toString(),
                            filedError.getDefaultMessage()
                    ))
                    .toList();
        }
    }
}
