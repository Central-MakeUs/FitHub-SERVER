package fithub.app.web.dto.responseDto;

import lombok.*;

public class HashTagResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HashtagDto{
        Long hashTagId;
        String name;
    }
}
