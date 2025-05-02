package org.com.jwtshop.domain.member.exception;

import org.com.jwtshop.global.error.ErrorCode;
import org.com.jwtshop.global.error.exception.BusinessException;

public class DuplicateMemberException extends BusinessException {
    public DuplicateMemberException(ErrorCode errorCode) {super(errorCode);}
}
