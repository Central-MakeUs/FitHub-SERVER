package fithub.app.service;

import fithub.app.domain.*;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    OAuthResult.OAuthResultDto kakaoOAuth(String socialId);

    OAuthResult.OAuthResultDto appleOAuth(String socialId);
    Optional<User> checkExistNickname(String nickname);

    List<ExerciseCategory> getExerciseList();

    User signUpPhoneNum(UserRequestDto.UserInfo request) throws IOException;

    User findByPhoneNum(String phoneNum);

    User updatePassword(String phoneNum,String password);

    void findByPhoneNumJoin(String phoNum);

    void findByPhoneNumPassChange(String phoneNum);

    String login(User user, String password);

    User socialInfoComplete(UserRequestDto.UserOAuthInfo request, User user) throws IOException;

    Page<Article> getMyArticlesNoCategory(Integer pageIndex, User user);
    Page<Article> getMyArticles(Integer pageIndex, User user, Integer categoryId);

    Page<Record> getMyRecordsNoCategory(Integer last, User user);
    Page<Record> getMyRecords(Integer pageIndex, User user, Integer categoryId);

    List<UserExercise> getMyExercises(User user);

    UserExercise patchMainExercise(User user, Integer categoryId);

    User findUser(Long userId);
}
