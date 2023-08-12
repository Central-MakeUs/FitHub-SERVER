package fithub.app.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash(value = "refreshToken", timeToLive = 1200000)
@RequiredArgsConstructor
public class RefreshToken {

    @Id
    private String token;

    private Long userId;

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }
}
