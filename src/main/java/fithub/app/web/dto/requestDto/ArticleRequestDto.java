package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ArticleRequestDto {

    @Getter @Setter
    public static class CreateArticleDto{
        @Override
        public String toString() {
            return "createArticleDto{" +
                    ", title='" + title + '\'' +
                    ", contents='" + contents + '\'' +
                    ", pictureList=" + pictureList +
                    '}';
        }
        @NotBlank
        String title;

        @NotBlank
        String contents;

        @NotBlank
        String exerciseTag;

        @Size(min = 0, max = 4)
        List<String> tagList;

        @Size(min = 0, max = 10)
        List<MultipartFile> pictureList = new ArrayList<>();
    }

    @Getter @Setter
    public static class UpdateArticleDto{

        @Override
        public String toString() {
            return "UpdateArticleDto{" +
                    "title='" + title + '\'' +
                    ", contents='" + contents + '\'' +
                    ", category=" + category +
                    ", exerciseTag='" + exerciseTag + '\'' +
                    ", hashTagList=" + hashTagList +
                    ", newPictureList=" + newPictureList +
                    ", remainPictureUrlList=" + remainPictureUrlList +
                    '}';
        }

        @NotBlank
        String title;
        @NotBlank
        String contents;

        @NotBlank
        Integer category;

        @NotBlank
        String exerciseTag;

        @Size(min = 0, max = 4)
        List<String> hashTagList;

        @Size(min = 0, max = 10)
        List<MultipartFile> newPictureList = new ArrayList<>();

        List<String> remainPictureUrlList = new ArrayList<>();

    }

    @Getter @Setter
    public static class DeleteListArticleDto{
        List<Long> articleIdList;
    }

    @Getter @Setter
    public static class ArticleLikeAlarmDto{
        String token;
    }
}
