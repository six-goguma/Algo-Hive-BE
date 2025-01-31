package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.auth.dto.LoginRequest;
import com.knu.algo_hive.auth.dto.LoginResponse;
import com.knu.algo_hive.auth.dto.RegisterRequest;
import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.BadRequestException;
import com.knu.algo_hive.common.exception.ConflictException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextHolderStrategy securityContextHolderStrategy;
    private final HttpSessionSecurityContextRepository securityContextRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MailService mailService;

    private final long ACCESS_THREE_MINUTES = 1000 * 60 * 3;
    private final long ACCESS_TEN_MINUTES = 1000 * 60 * 10;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        checkEmail(registerRequest.email());
        checkNickName(registerRequest.nickName());

        if (!Boolean.parseBoolean((String) redisTemplate.opsForHash().get(registerRequest.email(), "verified")))
            throw new BadRequestException("이메일 인증을 하지 않았습니다.");

        Member member = new Member(registerRequest.nickName(),
                registerRequest.email(),
                passwordEncoder.encode(registerRequest.password()));

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        return new LoginResponse(member.getNickName());
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) throw new ConflictException("중복된 이메일이 있습니다.");
    }

    @Transactional(readOnly = true)
    public void checkNickName(String nickname) {
        if (memberRepository.existsByNickName(nickname)) throw new ConflictException("중복된 닉네임이 있습니다.");
    }

    public void postCode(String email) throws MessagingException {
        checkEmail(email);
        Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));

        mailService.sendMail(email, code);

        redisTemplate.opsForHash().put(email, "code", code);
        redisTemplate.opsForHash().put(email, "verified", false);

        redisTemplate.expire(email, ACCESS_THREE_MINUTES, TimeUnit.MILLISECONDS);
    }

    public void verifyCode(String email, String code) {
        checkEmail(email);
        String storedCode = (String) redisTemplate.opsForHash().get(email, "code");

        if (storedCode.equals(code)) {
            redisTemplate.opsForHash().put(email, "verified", true);
            redisTemplate.expire(email, ACCESS_TEN_MINUTES, TimeUnit.MILLISECONDS);
            return;
        }

        throw new BadRequestException("인증번호를 잘못 입력하였습니다.");
    }
}
