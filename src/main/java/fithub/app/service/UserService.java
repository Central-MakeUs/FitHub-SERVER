package fithub.app.service;

import fithub.app.domain.User;
import fithub.app.utils.OAuthResult;

public interface UserService {
    OAuthResult.OAuthResultDto kakaoOAuth(String socialId);
}
