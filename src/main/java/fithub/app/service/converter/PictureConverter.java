package fithub.app.service.converter;

import fithub.app.domain.ArticleImage;
import fithub.app.web.dto.responseDto.PictureResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PictureConverter {

    public static PictureResponseDto.PictureDto toPictureDto(ArticleImage articleImage){
        return PictureResponseDto.PictureDto.builder()
                .pictureId(articleImage.getId())
                .pictureUrl(articleImage.getImageUrl())
                .build();
    }

    public static PictureResponseDto.PictureDtoList toPictureDtoList(List<ArticleImage> imageList){
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
