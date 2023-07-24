package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RecordRequestDto {

    @Getter
    @Setter
    public static class CreateRecordDto{

        @Override
        public String toString() {
            return "CreateRecordDto{" +
                    "contents='" + contents + '\'' +
                    ", exerciseTag='" + exerciseTag + '\'' +
                    ", hashTagList=" + hashTagList +
                    ", image=" + image +
                    '}';
        }

        @NotBlank
        String contents;

        @NotBlank
        String exerciseTag;

        @Size(min = 0, max = 4)
        List<String> hashTagList;

        MultipartFile image;
    }

    @Getter @Setter
    public static class updateRecordDto{
        @Override
        public String toString() {
            return "updateRecordDto{" +
                    "contents='" + contents + '\'' +
                    ", category=" + category +
                    ", exerciseTag='" + exerciseTag + '\'' +
                    ", hashTagList=" + hashTagList +
                    ", newImage=" + newImage +
                    ", remainImageUrl='" + remainImageUrl + '\'' +
                    '}';
        }

        String contents;

        Integer category;

        String exerciseTag;
        @Size(min = 0, max = 4)
        List<String> hashTagList;

        MultipartFile newImage;

        String remainImageUrl;
    }

    @Getter @Setter
    public static class deleteListRecordDto{
        List<Long> recordIdList;
    }
}
