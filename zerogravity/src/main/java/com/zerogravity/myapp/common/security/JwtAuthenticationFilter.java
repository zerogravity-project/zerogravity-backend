package com.zerogravity.myapp.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * JWT Authentication Filter for Spring Security
 * Intercepts every request and validates JWT token from cookies
 * If valid, sets authentication in SecurityContext
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            // 1. Extract JWT token from cookies
            String token = extractTokenFromCookie(request);

            // 2. If token exists and is valid, set authentication
            if (token != null) {
                Optional<Long> userIdOpt = jwtUtil.extractUserId(token);

                if (userIdOpt.isPresent()) {
                    Long userId = userIdOpt.get();

                    // Create Authentication object with userId as principal
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userId,                      // principal (userId)
                                    null,                        // credentials (not needed)
                                    Collections.emptyList()      // authorities (no roles for now)
                            );

                    // Set request details
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Store in SecurityContext (Spring Security will use this)
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Log error but don't block the request
            // Let Spring Security handle authorization
            logger.error("Cannot set user authentication: " + e.getMessage(), e);
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extract "token" value from HTTP request cookies
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
