package fithub.app.web.dto.responseDto;

import lombok.*;

public class SearchPreViewResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SearchPreViewDto{
        ArticleResponseDto.ArticleDtoList articlePreview;
        RecordResponseDto.recordDtoList recordPreview;
    }
}
