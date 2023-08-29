package com.backend.nutt.service;
import com.backend.nutt.domain.RefreshToken;
import com.backend.nutt.dto.response.Token;
import com.backend.nutt.exception.ErrorMessage;
import com.backend.nutt.exception.badrequest.TokenNotMatchException;
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
        String accessToken = generateAccessToken(email, name);
        String refreshToken = generateRefreshToken(email, name);

        saveRefreshToken(email, refreshToken);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateAccessToken(String email, String name) {
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

    private long getRemainSeconds(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        Date now = new Date();

        return expiration.getTime() - now.getTime();
    }

    public Token reissueToken(String refreshToken, String name) {
        String email = parseEmailFromToken(refreshToken);
        RefreshToken token = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new TokenNotMatchException(ErrorMessage.NOT_EXIST_TOKEN));

        if (token.getRefreshToken() == refreshToken) {
            return reissueTokens(refreshToken, name, email);
        }

        throw new TokenNotMatchException(ErrorMessage.NOT_MATCH_TOKEN);
    }

    private Token reissueTokens(String refreshToken, String name, String email) {
        String accessToken = generateAccessToken(email, name);
        if (lessThanReissueExpirationTimesLeft(refreshToken)) {
            String otherRefreshToken = generateRefreshToken(email, name);

            return Token.builder()
                    .accessToken(accessToken)
                    .refreshToken(otherRefreshToken)
                    .build();
        }

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
        return getRemainSeconds(refreshToken) < REFRESH_PERIOD;
    }
}
