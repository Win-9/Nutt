package com.backend.nutt.service;
import com.backend.nutt.domain.RefreshToken;
import com.backend.nutt.dto.response.Token;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.unavailable.TokenExpiredException;
import com.backend.nutt.repository.AccessTokenRepository;
import com.backend.nutt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final MemberDetailService memberDetailService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private static Key key;
    private final long ACCESS_PERIOD = 1000 * 60 * 60 * 24 * 7L;
    private final long REFRESH_PERIOD = 1000 * 60 * 60 * 24 * 14L;
    @PostConstruct
    private void setUPEncodeKey() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Token generateToken(String email, String name) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("name", name);

        Date nowDate = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(nowDate.getTime() + ACCESS_PERIOD))
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(nowDate.getTime() + REFRESH_PERIOD))
                .compact();

        saveRefreshToken(email, refreshToken);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessToken(String email, String name) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("name", name);
        Date nowDate = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(nowDate.getTime() + ACCESS_PERIOD))
                .compact();
    }

    private String generateRefreshToken(String email, String name) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("name", name);
        Date nowDate = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(nowDate.getTime() + REFRESH_PERIOD))
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = memberDetailService.loadUserByUsername(parseEmailFromToken(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String parseEmailFromToken(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return body.getSubject();
    }

    public boolean checkToken(String token) throws TokenExpiredException {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Optional.ofNullable(body.getExpiration())
                .map(expiration -> expiration.after(new Date()))
                .orElseThrow(() -> new TokenExpiredException(ErrorMessage.ACCESS_TOKEN_EXPIRED));
    }

    public RefreshToken saveRefreshToken(String email, String refreshToken) {
        RefreshToken token = new RefreshToken(email, refreshToken, REFRESH_PERIOD);

        return refreshTokenRepository.save(token);
    }
}
