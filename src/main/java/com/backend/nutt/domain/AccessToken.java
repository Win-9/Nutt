package com.backend.nutt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor
@RedisHash(value = "access_token")
@AllArgsConstructor
public class AccessToken {
    @Id
    private String id;  // 사용자 email
    private String accessToken;
}