package com.sms.authentication.util;

import com.sms.authentication.entity.User;
import com.sms.authentication.exception.CustomBadRequestException;
import com.sms.authentication.exception.CustomNotFoundException;
import com.sms.authentication.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access_time}")
    private int accessTime;
    @Value("${jwt.refresh_time}")
    private int refreshTime;
    private Key secretKey;

    private static final String CLAIM_ID="id";
    private static final String CLAIM_ROLE="role";

    @PostConstruct
    public void init(){
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(User user,int expirationTime){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_ID,user.getId());
        claims.put(CLAIM_ROLE,user.getRole());

        long currentMillis = System.currentTimeMillis();
        Date issueAt = new Date(currentMillis);
        Date expiration = new Date(currentMillis + expirationTime);

        return Jwts
                .builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setIssuedAt(issueAt)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

    }

    public String generateAccessToken(User user){
        return createToken(user,accessTime);
    }
    public String generateRefreshToken(User user){
        return createToken(user,refreshTime);
    }
    public Claims extractClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String extractSubject(String token){
        return extractClaims(token).getSubject();
    }
    public Date extractExpiration(String token){
        return extractClaims(token).getExpiration();
    }
    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token,String email){
        try {
            String subject = extractSubject(token);
            return subject.equals(email) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    public User getUserFromToken(String token){
        String email = extractSubject(token);
        if (!validateToken(token,email)){
            throw new CustomBadRequestException("Invalid or expired token");
        }
        return userRepository.findByEmail(email).orElseThrow(()-> new CustomNotFoundException("User not found"));
    }
}
