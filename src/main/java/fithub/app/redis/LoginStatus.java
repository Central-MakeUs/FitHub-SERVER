package fithub.app.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "loginStatus", timeToLive = 2400)
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginStatus {

    @Id
    private String accessToken;

    private Long LoginUserId;
}
