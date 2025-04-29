package org.com.jwtshop.domain.member.exception;

import org.com.jwtshop.global.error.ErrorCode;
import org.com.jwtshop.global.error.exception.BusinessException;

public class InvaildArgumentException extends BusinessException {
    public InvaildArgumentException(ErrorCode errorCode) {super(errorCode);
    }
}
