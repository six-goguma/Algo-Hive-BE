package com.knu.algo_hive.auth.controller;

import com.knu.algo_hive.auth.dto.NickNameRequest;
import com.knu.algo_hive.auth.dto.ProfileRequest;
import com.knu.algo_hive.auth.dto.ProfileResponse;
import com.knu.algo_hive.auth.service.CustomUserDetails;
import com.knu.algo_hive.auth.service.MyPageService;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExample;
import com.knu.algo_hive.common.annotation.ApiErrorCodeExamples;
import com.knu.algo_hive.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/mypage")
@Tag(name = "마이페이지 기능", description = "회원 정보 수정 및 회원 탈퇴 기능을 포함한다.")
@AllArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @Operation(summary = "프로필 조회 기능",
            description = "사용자의 프로필 이미지 주소를 조회한다. 리턴 값이 null일 경우 프로필 이미지가 없음을 의미한다.")
    @ApiErrorCodeExamples({ErrorCode.UNAUTHORIZED})
    @ApiResponse(responseCode = "200", description = "성공 시 이미지 주소가 반환된다.", useReturnTypeSchema = true)
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(myPageService.getProfile(userDetails.getMember()));
    }

    @Operation(summary = "프로필 등록 / 변경 기능",
            description = "사용자의 프로필 이미지를 등록한다. 이미 프로필 이미지가 있을 경우 제거하고 교체한다.")
    @ApiErrorCodeExamples({ErrorCode.IMAGE_DELETE_FAILED, ErrorCode.IMAGE_UPLOAD_FAILED,
            ErrorCode.UNAUTHORIZED, ErrorCode.FILE_SIZE_EXCEEDED,
            ErrorCode.IMAGE_NOT_UPLOADED, ErrorCode.INVALID_FILE_TYPE})
    @PostMapping("/profile")
    public ResponseEntity<ProfileResponse> postProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @ModelAttribute ProfileRequest profileRequest){
        return ResponseEntity.ok(myPageService.postProfile(userDetails.getMember(), profileRequest));
    }

    @Operation(summary = "프로필 삭제 기능",
            description = "사용자의 프로필 이미지를 삭제한다.")
    @ApiErrorCodeExample(ErrorCode.IMAGE_DELETE_FAILED)
    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(@AuthenticationPrincipal CustomUserDetails userDetails){
        myPageService.deleteProfile(userDetails.getMember());

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "닉네임 변경 기능",
            description = "사용자의 닉네임을 변경한다.")
    @ApiErrorCodeExamples({ErrorCode.DUPLICATE_NICK_NAME, ErrorCode.UNAUTHORIZED})
    @PutMapping("/nick-name")
    public ResponseEntity<?> putNickName(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @RequestBody NickNameRequest nickNameRequest){
        myPageService.putNickName(userDetails.getMember(), nickNameRequest);

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "회원 탈퇴 기능",
            description = "사용자의 정보를 제거하고 회원탈퇴를 수행한다.")
    @ApiErrorCodeExamples({ErrorCode.UNAUTHORIZED})
    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request, HttpServletResponse response){
        myPageService.withdrawal(userDetails.getMember(), request, response);

        return ResponseEntity.ok(ErrorCode.OK.getMessage());
    }

    @Operation(summary = "로그아웃 기능",
            description = "로그인한 사용자의 인증 권한을 제거한다.")
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        myPageService.logout(request, response);
        return ResponseEntity.ok(ErrorCode.OK);
    }

}
