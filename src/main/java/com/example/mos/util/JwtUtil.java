package com.example.mos.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTユーティリティクラス
 */
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String generateToken(Long employeeId, String loginId, Long storeId, Integer roleId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("employeeId", employeeId);
        claims.put("storeId", storeId);
        claims.put("roleId", roleId);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public String extractLoginId(String token) {
        return extractClaims(token).getSubject();
    }
    
    public Long extractEmployeeId(String token) {
        return extractClaims(token).get("employeeId", Long.class);
    }
    
    public Long extractStoreId(String token) {
        return extractClaims(token).get("storeId", Long.class);
    }
    
    public Integer extractRoleId(String token) {
        return extractClaims(token).get("roleId", Integer.class);
    }
    
    public Boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    
    public Boolean validateToken(String token, String loginId) {
        final String extractedLoginId = extractLoginId(token);
        return (extractedLoginId.equals(loginId) && !isTokenExpired(token));
    }
}
