package com.knu.algo_hive.auth.controller;

import com.knu.algo_hive.auth.dto.*;
import com.knu.algo_hive.auth.service.MemberService;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "인증/인가 기능", description = "회원 인증 및 인가 기능을 위한 api")
public class AuthRestController {
    private final MemberService memberService;

    @Operation(summary = "회원 가입 기능",
            description = "닉네임, 이메일, 비밀번호를 입력받아 회원가입을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.DUPLICATE_EMAIL, ErrorCode.DUPLICATE_NICK_NAME, ErrorCode.NOT_VERIFY_EMAIL})
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        memberService.register(registerRequest);

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "로그인 기능",
            description = "이메일, 비밀번호를 입력받아 로그인을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiResponse(responseCode = "200", description = "로그인 성공 시 닉네임이 반환된다.")
    @ApiErrorCodeExamples({ErrorCode.UNAUTHORIZED})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(memberService.login(loginRequest, request, response));
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 발급",
            description = "이메일을 입력받아 인증번호를 생성한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.DUPLICATE_EMAIL})
    @PostMapping("/code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailRequest emailRequest) throws MessagingException {
        memberService.postCode(emailRequest.email());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 확인",
            description = "이메일과 인증번호를 입력받아 인증번호를 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.DUPLICATE_EMAIL,ErrorCode.WRONG_VERIFICATION_CODE})
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyCode(verificationRequest.email(), verificationRequest.code());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "닉네임 중복 확인",
            description = "닉네임이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK,ErrorCode.DUPLICATE_NICK_NAME})
    @PostMapping("/nickname")
    public ResponseEntity<?> checkNickName(@RequestBody NicknameRequest nickNameRequest) {
        memberService.checkNickName(nickNameRequest.nickname());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "이메일 중복 확인",
            description = "이메일이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @ApiErrorCodeExamples({ErrorCode.OK, ErrorCode.DUPLICATE_EMAIL})
    @PostMapping("/email")
    public ResponseEntity<?> checkEmail(@RequestBody EmailRequest emailRequest) {
        memberService.checkEmail(emailRequest.email());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }
}
