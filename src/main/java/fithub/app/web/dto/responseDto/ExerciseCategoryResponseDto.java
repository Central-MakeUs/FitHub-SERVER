package fithub.app.web.dto.responseDto;

import lombok.*;

import java.util.List;

public class ExerciseCategoryResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryDto{
        Integer categoryId;
        String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryFullDto{
        Integer categoryId;
        String categoryImage;
        String name;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CategoryFullDtoList{
        List<CategoryFullDto> categoryList;
        Integer size;
    }


}
