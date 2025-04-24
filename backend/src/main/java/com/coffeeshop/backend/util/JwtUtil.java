package com.coffeeshop.backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtil {

    private static final Logger LOGGER = Logger.getLogger(JwtUtil.class.getName());

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms:86400000}") // 1 ngày mặc định
    private long expirationMs;

    private Key key;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT secret is not configured properly.");
        }
        // Kiểm tra độ dài của secret key, nếu không đủ dài thì sử dụng cách khác để tạo key
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long.");
        }

        // Nếu key của bạn là chuỗi Base64, bạn có thể giải mã nó và đảm bảo đủ độ dài
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            // Đảm bảo rằng key phải có đủ độ dài 256 bits trở lên cho thuật toán HMAC
            if (decodedKey.length < 32) {
                throw new IllegalArgumentException("Decoded JWT secret key is not secure enough.");
            }
            this.key = Keys.hmacShaKeyFor(decodedKey);
        } catch (IllegalArgumentException e) {
            // Nếu secret key không phải là Base64, hoặc không hợp lệ, tạo key ngẫu nhiên
            LOGGER.warning("Invalid base64 encoded secret key, generating a secure random key.");
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Tạo khóa an toàn với độ dài 512 bit
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public void validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        if (!username.equals(userDetails.getUsername()) || isTokenExpired(token)) {
            throw new InvalidJwtException("Token không hợp lệ hoặc đã hết hạn", null);
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            LOGGER.warning("❌ Token đã hết hạn");
            throw new InvalidJwtException("Token đã hết hạn", e);
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.severe("❌ Token không hợp lệ: " + e.getMessage());
            throw new InvalidJwtException("Token không hợp lệ", e);
        }
    }

    public static class InvalidJwtException extends RuntimeException {
        public InvalidJwtException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}