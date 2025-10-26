package com.zerogravity.myapp.security;

import com.zerogravity.myapp.exception.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * @AuthUserId 어노테이션을 처리하는 ArgumentResolver
 * HTTP 쿠키에서 JWT를 추출하고, 해당 토큰에서 userId를 가져온 후
 * 자동으로 메서드 파라미터에 주입한다.
 */
@Component
public class AuthUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthUserIdArgumentResolver(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 이 ArgumentResolver가 처리할 수 있는 파라미터인지 판단
     * @AuthUserId 어노테이션이 있으면 처리 가능
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUserId.class);
    }

    /**
     * 실제 userId 값을 추출하고 반환
     * 1. HTTP 쿠키에서 "token" 추출
     * 2. JWTUtil을 사용해서 토큰에서 userId 추출
     * 3. userId가 없으면 UnauthorizedException 발생
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {

        // 1. HttpServletRequest에서 쿠키 추출
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = extractTokenFromCookie(request);

        // 2. JWT에서 userId 추출
        Optional<Long> userIdOpt = jwtUtil.extractUserId(token);

        // 3. userId가 없으면 UnauthorizedException 발생
        if (userIdOpt.isEmpty()) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
        }

        // 4. userId 반환 → Spring이 이 값을 메서드 파라미터에 주입
        return userIdOpt.get();
    }

    /**
     * HTTP 요청의 쿠키에서 "token" 값을 추출
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request == null || request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
