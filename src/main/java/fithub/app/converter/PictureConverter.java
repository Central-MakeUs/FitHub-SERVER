package fithub.app.converter;

import fithub.app.domain.ArticleImages;
import fithub.app.web.dto.responseDto.PictureResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PictureConverter {

    public static PictureResponseDto.PictureDto toPictureDto(ArticleImages articleImages){
        return PictureResponseDto.PictureDto.builder()
                .pictureId(articleImages.getId())
                .pictureUrl(articleImages.getImageUrl())
                .build();
    }

    public static PictureResponseDto.PictureDtoList toPictureDtoList(List<ArticleImages> imageList){
        List<PictureResponseDto.PictureDto> pictureDtoList =
                imageList.stream()
                        .map(image -> toPictureDto(image))
                        .collect(Collectors.toList());

        return PictureResponseDto.PictureDtoList.builder()
                .pictureList(pictureDtoList)
                .size(pictureDtoList.size())
                .build();

    }
}
