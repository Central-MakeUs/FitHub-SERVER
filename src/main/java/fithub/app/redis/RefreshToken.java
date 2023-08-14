package fithub.app.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;


@RedisHash(value = "refreshToken", timeToLive = 1800000)
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String refreshToken;

    private Long userId;

    private LocalDateTime expireTime;

    Boolean isOAuth;

    public String getToken() {
        return refreshToken;
    }

    public LocalDateTime getExpireTime(){
        return expireTime;
    }

    public Long getUserId() {
        return userId;
    }
}
