package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

public class RootRequestDto {

    @Getter @Setter
    public static class AutoLoginDto{

        @Nullable
        String FCMToken;
    }

    @Getter @Setter
    public static class SaveImageAsUrlDto{
        List<MultipartFile> image;
    }
}
