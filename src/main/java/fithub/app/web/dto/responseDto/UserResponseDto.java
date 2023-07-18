package fithub.app.web.dto.responseDto;

import fithub.app.domain.UserExercise;
import lombok.*;

import java.util.List;

public class UserResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserDto{
        private Long userId;
        private String email;
        private String name;
        private String nickname;
        private String profileUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ArticleUserDto{
        Long ownerId;
        String ProfileUrl;
        String nickname;
        UserExerciseResponseDto.UserExerciseDto mainExerciseInfo;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecordUserDto{
        Long ownerId;
        String ProfileUrl;
        String nickname;
        UserExerciseResponseDto.UserExerciseDto mainExerciseInfo;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserListDto{
        private List<UserDto> userList;
        private Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinUserDto{
        private Long userId;
        private String nickname;
        private String accessToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SocialInfoDto{
        private Long userId;
        private String nickname;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OauthDto{
        private String accessToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PassChangeDto{
        private String newPass;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginDto{
        private String accessToken;
    }
}
