package fithub.app.web.dto.responseDto;

import lombok.*;

public class PictureResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PictureDto{
        Long pictureId;
        String pictureUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PictureDtoList{
        PictureDto[] pictureList;
        Long size;
    }
}
