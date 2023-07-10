package fithub.app.converter;

import fithub.app.domain.ExerciseCategory;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.web.dto.responseDto.ExerciseCategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ExerciseCategoryResponseDto.CategoryFullDto toCategoryFullDto(ExerciseCategory exerciseCategory){
        return ExerciseCategoryResponseDto.CategoryFullDto.builder()
                .categoryId(exerciseCategory.getId())
                .categoryImage(exerciseCategory.getImageUrl())
                .name(exerciseCategory.getName())
                .build();
    }

<<<<<<< HEAD
    public static ExerciseCategoryResponseDto.CategoryFullDtoList toCategoryFullDtoList(List<ExerciseCategory> exerciseCategories){
        List<ExerciseCategoryResponseDto.CategoryFullDto> categoryFullDtoList =
            exerciseCategories.stream()
                    .map(exerciseCategory -> toCategoryFullDto(exerciseCategory))
                    .collect(Collectors.toList());
=======
    public static ExerciseCategoryResponseDto.CategoryFullDtoList toCategoryFullDtoList(List<ExerciseCategory> exerciseCategoryList){
        List<ExerciseCategoryResponseDto.CategoryFullDto> categoryFullDtoList =
                exerciseCategoryList
                        .stream()
                        .map(exerciseCategory -> toCategoryFullDto(exerciseCategory))
                        .collect(Collectors.toList());
>>>>>>> 00c5bf386e0b5d206853432ac967c4f94d9e64df

        return ExerciseCategoryResponseDto.CategoryFullDtoList.builder()
                .categoryList(categoryFullDtoList)
                .size(categoryFullDtoList.size())
                .build();
    }
<<<<<<< HEAD

=======
>>>>>>> 00c5bf386e0b5d206853432ac967c4f94d9e64df
}
