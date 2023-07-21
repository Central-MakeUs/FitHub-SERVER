package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class CommentsRequestDto {

    @Getter @Setter
    public static class CreateCommentDto{
        String contents;
    }

    @Getter @Setter
    public static class UpdateCommentDto{
        @NotBlank
        String contents;
    }
}
