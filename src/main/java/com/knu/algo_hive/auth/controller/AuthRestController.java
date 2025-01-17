package com.knu.algo_hive.auth.controller;

import com.knu.algo_hive.auth.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "인증/인가 기능", description = "회원 인증 및 인가 기능을 위한 api")
public class AuthRestController {
    @Operation(summary = "회원 가입 기능(미구현)",
            description = "닉네임, 이메일, 비밀번호를 입력받아 회원가입을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerRequest){

        return "성공";
    }

    @Operation(summary = "로그인 기능(미구현)",
            description = "이메일, 비밀번호를 입력받아 로그인을 진행한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){

        return "성공";
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 발급(미구현)",
            description = "이메일을 입력받아 인증번호를 생성한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/code")
    public String sendVerificationCode(@RequestBody EmailRequest emailRequest){

        return "성공";
    }

    @Operation(summary = "이메일 인증 기능 - 인증번호 확인(미구현)",
            description = "이메일과 인증번호를 입력받아 인증번호를 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/verify")
    public String verifyEmail(@RequestBody VerificationRequest verificationRequest){

        return "성공";
    }

    @Operation(summary = "닉네임 중복 확인(미구현)",
            description = "닉네임이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/nick-name")
    public String checkNickName(@RequestBody NickNameRequest nickNameRequest){

        return "성공";
    }

    @Operation(summary = "이메일 중복 확인(미구현)",
            description = "이메일이 다른 사용자와 중복되는지 확인한다.",
            security = @SecurityRequirement(name = "Session 제외"))
    @PostMapping("/email")
    public String checkEmail(@RequestBody EmailRequest emailRequest){

        return "성공";
    }
}
