package fithub.app.converter;

import fithub.app.domain.User;
import fithub.app.domain.UserExercise;
import fithub.app.web.dto.responseDto.UserExerciseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserExerciseConverter {

    public static UserExerciseResponseDto.UserExerciseDto toUserExerciseDto(User user){
        UserExercise mainExercise = user.getMainExercise();
        return mainExercise == null ? null : UserExerciseResponseDto.UserExerciseDto.builder()
                .category(user.getMainExercise().getExerciseCategory().getName())
                .GradeName(user.getMainExercise().getGrade().getName())
                .level(user.getMainExercise().getGrade().getLevel())
                .build();
    }
}
