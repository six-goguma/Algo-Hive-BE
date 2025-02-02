package com.knu.algo_hive.common.exception;

public class UnauthorizedException extends RuntimeException {

    ErrorCode errorCode;
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException(ErrorCode errorcode){this.errorCode = errorcode;}
}
