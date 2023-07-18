package fithub.app.web.dto.responseDto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


public class UserExerciseResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UserExerciseDto{
        @Schema(description = "주력 운동의 카테고리")
        String category;
        @Schema(description = "등급")
        String GradeName;
        @Schema(description = "레벨")
        Integer level;
    }
}
