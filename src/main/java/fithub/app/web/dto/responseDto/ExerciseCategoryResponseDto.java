package fithub.app.web.dto.responseDto;

import lombok.*;

public class ExerciseCategoryResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryDto{
        Integer categoryId;
        String name;
    }
}
