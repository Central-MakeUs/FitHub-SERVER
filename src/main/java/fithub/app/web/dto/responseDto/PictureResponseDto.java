package fithub.app.web.dto.responseDto;

import lombok.*;

import java.util.List;

public class PictureResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PictureDto{
        Integer pictureId;
        String pictureUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PictureDtoList{
        List<PictureDto> pictureList;
        Integer size;
    }
}
