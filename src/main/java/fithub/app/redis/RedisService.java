package fithub.app.redis;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.JwtAuthenticationException;
import fithub.app.base.exception.handler.UserException;
import fithub.app.domain.User;
import fithub.app.redis.repository.LoginTokenRepository;
import fithub.app.redis.repository.RefreshTokenRepository;
import fithub.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginTokenRepository loginTokenRepository;

    public RefreshToken createRefreshOAuth(String socialId){
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));

        String refreshToken = UUID.randomUUID().toString();
        Long id = user.getId();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = currentTime.plus(2, ChronoUnit.WEEKS);
        return refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .expireTime(expireTime)
                .userId(id)
                .isOAuth(true)
                .build());
    }

    public RefreshToken createRefresh(String phoneNum){
        User user = userRepository.findByPhoneNum(phoneNum).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));

        String refreshToken = UUID.randomUUID().toString();
        Long id = user.getId();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expireTime = currentTime.plus(2, ChronoUnit.WEEKS);
        return refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(refreshToken)
                .expireTime(expireTime)
                .userId(id)
                .isOAuth(false)
                .build());
    }

    public RefreshToken regenerateRefreshToken(String refreshToken){
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new JwtAuthenticationException(Code.JWT_REFRESH_TOKEN_EXPIRED));
        LocalDateTime expireTime = findRefreshToken.getExpireTime();
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime expireDeadLine = current.plusMinutes(30);

        User user = userRepository.findById(findRefreshToken.getUserId()).orElseThrow(() -> new UserException(Code.MEMBER_NOT_FOUND));

        if(current.isAfter(expireTime))
            throw new JwtAuthenticationException(Code.JWT_REFRESH_TOKEN_EXPIRED);

        if(expireTime.isAfter(expireDeadLine))
            return findRefreshToken;
        else
            return findRefreshToken.getIsOAuth() ? createRefreshOAuth(user.getSocialId()) : createRefresh(user.getPhoneNum());
    }

    public void saveLoginStatus(Long userId, String accessToken){
        LoginStatus loginStatus = LoginStatus.builder()
                .LoginUserId(userId)
                .accessToken(accessToken)
                .build();
        loginTokenRepository.save(loginStatus);
    }

    public void logoutLoginStatus(String accessToken){
        LoginStatus loginStatus = loginTokenRepository.findById(accessToken).get();
        loginTokenRepository.delete(loginStatus);
    }

    public Boolean validateLogOutedToken(String accessToken){
        Optional<LoginStatus> byAccessToken = loginTokenRepository.findById(accessToken);

        return byAccessToken.isPresent();
    }
}
