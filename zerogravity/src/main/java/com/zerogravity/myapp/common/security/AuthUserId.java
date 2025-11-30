package com.zerogravity.myapp.common.security;

import io.swagger.v3.oas.annotations.Parameter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for automatically injecting authenticated user's userId
 * Extracts userId from JWT token and injects it into method parameter
 *
 * Usage example:
 * @GetMapping("/me")
 * public ResponseEntity<?> getProfile(@AuthUserId Long userId) { ... }
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface AuthUserId {
}
