package com.backend.nutt.service;
import com.backend.nutt.dto.response.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final MemberDetailService memberDetailService;

    private final String secretKey = "NUTTNUTTNUTTNUTSSSSEEECCCRRREEETTKKKEEYYNNUTNUUT";
    private static Key key;
    private final long ACCESS_PERIOD = 10000000L;
    private final long REFRESH_PERIOD = 10000000L;

    @PostConstruct
    private void setUPEncodeKey() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Token generateToken(String email, String name) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("name", name);

        Date nowDate = new Date();

        return Token.builder()
                .accessToken(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(nowDate)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .setExpiration(new Date(nowDate.getTime() + ACCESS_PERIOD))
                        .compact())
                .refreshToken(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(nowDate)
                        .signWith(key, SignatureAlgorithm.HS256)
                        .setExpiration(new Date(nowDate.getTime() + REFRESH_PERIOD))
                        .compact()).build();
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

    public boolean checkToken(String token) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        try {
            return body.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
