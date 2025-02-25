package com.knu.algo_hive.common.exception;

public class ForbiddenException extends RuntimeException {
    ErrorCode errorCode;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(ErrorCode errorcode) {
        this.errorCode = errorcode;
    }
}
