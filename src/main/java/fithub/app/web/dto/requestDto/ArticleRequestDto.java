package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class ArticleRequestDto {

    @Getter @Setter
    public static class uploadArticlePictureDto{
        private MultipartFile file;
    }

    @Getter @Setter
    public static class uploadArticleDto{
        Long categoryId;
        String title;
        String contents;
    }

    @Getter @Setter
    public static class updateArticleDto{
        String title;
        String contents;
    }
}
