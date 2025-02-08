package com.knu.algo_hive.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knu.algo_hive.common.exception.ErrorCode;
import com.knu.algo_hive.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        String acceptHeader = request.getHeader("Accept");
//        boolean isAjax = acceptHeader != null && acceptHeader.contains("application/json");
//
//        if (!isAjax) {
//            response.sendRedirect("/");
//            return;
//        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorCode.getHttpStatus(), errorCode.getMessage());
        problemDetail.setTitle("Unauthorized");

        response.getWriter().write(objectMapper.writeValueAsString(problemDetail));
    }
}