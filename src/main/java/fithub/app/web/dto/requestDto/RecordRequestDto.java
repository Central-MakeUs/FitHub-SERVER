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
                    ", tagList=" + tagList +
                    ", exerciseTag='" + exerciseTag + '\'' +
                    ", image=" + image +
                    '}';
        }

        @NotBlank
        String contents;

        @Size(min = 0, max = 4)
        List<String> tagList;

        @NotBlank
        String exerciseTag;

        MultipartFile image;
    }

    @Getter @Setter
    public static class updateRecordDto{
        String title;
        String contents;
    }

    @Getter @Setter
    public static class deleteListRecordDto{
        Long[] recordIdList;
    }
}
