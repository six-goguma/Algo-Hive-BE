package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.auth.dto.NicknameRequest;
import com.knu.algo_hive.auth.dto.ProfileRequest;
import com.knu.algo_hive.auth.dto.ProfileResponse;
import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.entity.Profile;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.auth.repository.ProfileRepository;
import com.knu.algo_hive.common.exception.BadRequestException;
import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MyPageService {
    @Value("${file.path}")
    private String uploadFolder;
    @Value("${image.url}")
    private String imageUrl;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif"
    );
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final HttpSessionSecurityContextRepository securityContextRepository;

    public MyPageService(ProfileRepository profileRepository,
                         MemberRepository memberRepository,
                         SecurityContextHolderStrategy securityContextHolderStrategy,
                         HttpSessionSecurityContextRepository securityContextRepository){
        this.profileRepository = profileRepository;
        this.memberRepository = memberRepository;
        this.securityContextRepository = securityContextRepository;
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

    @Transactional
    public void putNickName(Member member, NicknameRequest nickNameRequest){
        if(memberRepository.existsByNickname(nickNameRequest.nickname()))
            throw new ConflictException(ErrorCode.DUPLICATE_NICK_NAME);

        member.putNickname(nickNameRequest.nickname());
        memberRepository.save(member);
    }

    @Transactional
    public ProfileResponse postProfile(Member member, ProfileRequest profileRequest){
        MultipartFile file = profileRequest.file();
        if(file.isEmpty()) throw new ConflictException(ErrorCode.IMAGE_NOT_UPLOADED);
        if(file.getSize() > MAX_FILE_SIZE) throw new ConflictException(ErrorCode.FILE_SIZE_EXCEEDED);

        String mimeType = file.getContentType();
        if (!ALLOWED_MIME_TYPES.contains(mimeType)) throw new ConflictException(ErrorCode.INVALID_FILE_TYPE);

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + imageFileName);

        try {
            file.transferTo(destinationFile);
            Profile profile = profileRepository.findByMember(member).orElse(new Profile(member));
            if(profile.getUrl() != null) deleteStoredImage(uploadFolder + profile.getUrl());
            profile.updateUrl(imageFileName);

            profileRepository.save(profile);
            return new ProfileResponse(imageUrl + profile.getUrl());
        } catch (IOException e) {
            throw new ConflictException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void deleteProfile(Member member){
        Profile profile = profileRepository.findByMember(member)
                .orElseThrow(() -> new BadRequestException(ErrorCode.IMAGE_DELETE_FAILED));

        deleteStoredImage(uploadFolder + profile.getUrl());
        profileRepository.delete(profile);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(Member member) {
        Optional<Profile> profile = profileRepository.findByMember(member);

        return profile.map(value -> new ProfileResponse(imageUrl + value.getUrl()))
                .orElseGet(() -> new ProfileResponse(null));
    }

    @Transactional
    public void withdrawal(Member member, HttpServletRequest request, HttpServletResponse response){
        logout(request, response);
        memberRepository.delete(member);
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();

        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        securityContextRepository.saveContext(context, request, response);
    }

    public void deleteStoredImage(String path){
        File deletedFile = new File(path);
        if(!deletedFile.delete()) throw new ConflictException(ErrorCode.IMAGE_DELETE_FAILED);
    }
}
