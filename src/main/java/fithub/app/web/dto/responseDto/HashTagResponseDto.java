package fithub.app.web.dto.responseDto;

import io.swagger.models.auth.In;
import lombok.*;

import java.util.List;

public class HashTagResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HashtagDto{
        Long hashTagId;
        String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HashtagDtoList{
        List<HashtagDto> hashtags;
        Integer size;
    }
}
