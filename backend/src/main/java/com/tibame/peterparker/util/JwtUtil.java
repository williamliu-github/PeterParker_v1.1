package com.tibame.peterparker.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    // 初始化 SECRET_KEY
    private SecretKey SECRET_KEY;

    // 從配置檔案中注入 SECRET_KEY
    @Value("${jwt.secret-key}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // 從 token 中提取用戶名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 從 token 中提取過期時間
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 泛型方法，用於從 token 中提取特定的 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 從 token 中提取所有的 claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 檢查 token 是否已過期
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 生成 token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // 創建一個新的 token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
    }

    // 驗證 token 的有效性
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
