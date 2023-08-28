package com.backend.nutt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token")
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String id;
    private Member member;

    @TimeToLive
    private Long expiration;

}