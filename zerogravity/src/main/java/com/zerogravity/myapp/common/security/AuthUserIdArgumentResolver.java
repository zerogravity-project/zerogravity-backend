package com.zerogravity.myapp.common.security;

import com.zerogravity.myapp.auth.exception.UnauthorizedException;
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
 * ArgumentResolver for handling @AuthUserId annotation
 * Extracts JWT from Authorization header or HTTP cookies, gets userId from the token,
 * and automatically injects it into method parameters.
 * Priority: Authorization header (Bearer token) > Cookie
 */
@Component
public class AuthUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthUserIdArgumentResolver(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Determines if this ArgumentResolver can handle the parameter
     * Can handle if @AuthUserId annotation is present
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUserId.class);
    }

    /**
     * Extracts and returns the actual userId value
     * 1. Extract JWT from Authorization header or cookies
     * 2. Use JWTUtil to extract userId from token
     * 3. Throw UnauthorizedException if userId is not found
     */
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {

        // 1. Extract token from Authorization header or cookies
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = extractToken(request);

        // 2. Extract userId from JWT
        Optional<Long> userIdOpt = jwtUtil.extractUserId(token);

        // 3. Throw UnauthorizedException if userId is not found
        if (userIdOpt.isEmpty()) {
            throw new UnauthorizedException("Invalid token. Please login again.");
        }

        // 4. Return userId → Spring injects this value into method parameter
        return userIdOpt.get();
    }

    /**
     * Extract JWT token from request
     * Priority: 1. Authorization header (Bearer token) → 2. Cookie
     */
    private String extractToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        // 1. Try Authorization header first
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 2. Fallback to cookie
        return extractTokenFromCookie(request);
    }

    /**
     * Extract "token" value from HTTP request cookies
     */
    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
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
