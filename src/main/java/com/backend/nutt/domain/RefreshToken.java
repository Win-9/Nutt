package com.backend.nutt.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token")
public class RefreshToken {
    @Id
    private String id;
    private Member member;
    private LocalDateTime expirationDate;

    @Builder
    public RefreshToken(String id, Member member, LocalDateTime expirationDate) {
        this.id = id;
        this.member = member;
        this.expirationDate = expirationDate;
    }
}