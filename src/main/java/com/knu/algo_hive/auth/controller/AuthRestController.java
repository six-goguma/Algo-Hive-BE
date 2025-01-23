package com.knu.algo_hive.auth.controller;

import com.knu.algo_hive.auth.dto.*;
import com.knu.algo_hive.auth.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "회원 가입 기능(미구현)",
            description = "닉네임, 이메일, 비밀번호를 입력받아 회원가입을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        memberService.register(registerRequest);

        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    @Operation(summary = "로그인 기능",
            description = "이메일, 비밀번호를 입력받아 로그인을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        memberService.login(loginRequest, request, response);

        return ResponseEntity.ok("로그인에 성공하였습니다.");
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 발급",
            description = "이메일을 입력받아 인증번호를 생성한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailRequest emailRequest) throws MessagingException {
        memberService.postCode(emailRequest.email());

        return ResponseEntity.ok("인증번호가 발급되었습니다.");
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 확인",
            description = "이메일과 인증번호를 입력받아 인증번호를 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerificationRequest verificationRequest) {
        memberService.verifyCode(verificationRequest.email(), verificationRequest.code());

        return ResponseEntity.ok("이메일 인증에 성공하였습니다.");
    }

    @Operation(summary = "닉네임 중복 확인",
            description = "닉네임이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/nick-name")
    public ResponseEntity<?> checkNickName(@RequestBody NickNameRequest nickNameRequest) {
        memberService.checkNickName(nickNameRequest.nickName());

        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @Operation(summary = "이메일 중복 확인",
            description = "이메일이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/email")
    public ResponseEntity<?> checkEmail(@RequestBody EmailRequest emailRequest) {
        memberService.checkEmail(emailRequest.email());

        return ResponseEntity.ok("사용 가능한 이메일입니다.");
    }
}
