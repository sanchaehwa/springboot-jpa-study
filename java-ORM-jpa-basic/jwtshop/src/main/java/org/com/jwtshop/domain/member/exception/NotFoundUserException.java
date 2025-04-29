package org.com.jwtshop.domain.member.exception;

import org.com.jwtshop.global.error.ErrorCode;
import org.com.jwtshop.global.error.exception.BusinessException;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException(ErrorCode errorCode) { super(errorCode); }
}
