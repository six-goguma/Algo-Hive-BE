package com.knu.algo_hive.auth.service;

import com.knu.algo_hive.auth.dto.LoginRequest;
import com.knu.algo_hive.auth.dto.LoginResponse;
import com.knu.algo_hive.auth.dto.RegisterRequest;
import com.knu.algo_hive.auth.entity.Member;
import com.knu.algo_hive.auth.repository.MemberRepository;
import com.knu.algo_hive.common.exception.BadRequestException;
import com.knu.algo_hive.common.exception.ConflictException;
import com.knu.algo_hive.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
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
    private final RedissonClient redissonClient;

    private final long ACCESS_THREE_MINUTES = 1000 * 60 * 3;
    private final long ACCESS_FIVE_MINUTES = 1000 * 60 * 5;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        checkEmail(registerRequest.email());
        checkNickName(registerRequest.nickname());

        if (!Boolean.parseBoolean((String) redisTemplate.opsForHash().get(registerRequest.email(), "verified")))
            throw new BadRequestException(ErrorCode.NOT_VERIFY_EMAIL);

        Member member = new Member(registerRequest.nickname(),
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

        return new LoginResponse(member.getNickname());
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if (memberRepository.existsByEmail(email)) throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
    }

    @Transactional(readOnly = true)
    public void checkNickName(String nickname) {
        if (memberRepository.existsByNickname(nickname)) throw new ConflictException(ErrorCode.DUPLICATE_NICK_NAME);
    }

    @Async("taskExecutor")
    public void postCode(String email) throws MessagingException {
        checkEmail(email);

        RLock lock = redissonClient.getLock("email:" + email);
        try {
            if (lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                Random random = new Random();
                String code = String.format("%04d", random.nextInt(10000));

                mailService.sendMail(email, code);

                redisTemplate.opsForHash().put(email, "code", code);
                redisTemplate.opsForHash().put(email, "verified", false);

                redisTemplate.expire(email, ACCESS_THREE_MINUTES, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            throw new BadRequestException(ErrorCode.LOCK_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void verifyCode(String email, String code) {
        checkEmail(email);
        String storedCode = (String) redisTemplate.opsForHash().get(email, "code");

        if (storedCode.equals(code)) {
            redisTemplate.opsForHash().put(email, "verified", true);
            redisTemplate.expire(email, ACCESS_FIVE_MINUTES, TimeUnit.MILLISECONDS);
            return;
        }

        throw new BadRequestException(ErrorCode.WRONG_VERIFICATION_CODE);
    }
}
