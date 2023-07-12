package fithub.app.service;

import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.responseDto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    OAuthResult.OAuthResultDto kakaoOAuth(String socialId);
    Optional<User> checkExistNickname(String nickname);

    List<ExerciseCategory> getExerciseList();
}
