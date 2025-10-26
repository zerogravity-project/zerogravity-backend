package com.zerogravity.myapp.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증된 사용자의 userId를 자동으로 주입하는 어노테이션
 * JWT 토큰에서 userId를 추출하여 메서드 파라미터에 주입
 *
 * 사용 예시:
 * @GetMapping("/me")
 * public ResponseEntity<?> getProfile(@AuthUserId Long userId) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthUserId {
}
