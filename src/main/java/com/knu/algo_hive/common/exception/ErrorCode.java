package com.knu.algo_hive.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //OK
    OK(HttpStatus.OK, "성공했습니다."),

    //Auth
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다."),
    DUPLICATE_NICK_NAME(HttpStatus.CONFLICT, "중복된 닉네임이 있습니다."),
    NOT_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이메일 인증을 하지 않았습니다."),
    WRONG_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 번호를 잘못 입력하였습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 시 입력한 정보가 틀렸거나, 로그인을 하지 않았습니다."),

    //Mail
    FAIL_SEND_EMAIL(HttpStatus.CONFLICT, "이메일 전송 중 에러가 발생하였습니다."),

    //Chat
    ROOM_NAME_NOT_EXISTS(HttpStatus.NOT_FOUND, "해당 roomName은 존재하지 않습니다."),
    ROOM_NOT_EXISTS(HttpStatus.NOT_FOUND, "roomName에 해당하는 방이 없습니다."),

    //Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable throwable) {
        return this.getMessage(this.getMessage(this.getMessage() + " - " + throwable.getMessage()));
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
}
