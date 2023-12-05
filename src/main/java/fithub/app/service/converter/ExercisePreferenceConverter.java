package fithub.app.service.converter;

import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ExercisePreference;
import fithub.app.web.dto.requestDto.UserRequestDto;

public class ExercisePreferenceConverter {

    public static ExercisePreference toExercisePreference(User user, ExerciseCategory exerciseCategory){
        return ExercisePreference.builder()
                .user(user)
                .exerciseCategory(exerciseCategory)
                .build();
    }
}
