package fithub.app.web.dto.responseDto;

import lombok.*;


public class UserExerciseResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserExerciseDto{
        String category;
        String GradeName;
        Integer level;
    }
}
