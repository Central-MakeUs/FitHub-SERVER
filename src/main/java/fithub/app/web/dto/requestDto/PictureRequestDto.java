package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class PictureRequestDto {

    @Getter
    @Setter
    public static class uploadArticlePictureDto{
        private MultipartFile file;
    }
}
