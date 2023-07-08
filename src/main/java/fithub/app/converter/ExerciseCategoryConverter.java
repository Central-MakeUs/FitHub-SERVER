package fithub.app.converter;

import fithub.app.domain.ExerciseCategory;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.web.dto.responseDto.ExerciseCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ExerciseCategoryConverter {
    private final ExerciseCategoryRepository exerciseCategoryRepository;
    private static ExerciseCategoryRepository staticExerciseCategoryRepository;

    @PostConstruct
    public void init() {
        staticExerciseCategoryRepository = this.exerciseCategoryRepository;
    }

    public static ExerciseCategoryResponseDto.CategoryDto toCategoryDto(ExerciseCategory exerciseCategory){
        return ExerciseCategoryResponseDto.CategoryDto.builder()
                .categoryId(exerciseCategory.getId())
                .name(exerciseCategory.getName())
                .build();
    }
}
