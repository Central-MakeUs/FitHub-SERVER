package fithub.app.web.dto.requestDto;

import lombok.Getter;
import lombok.Setter;

public class CommentsRequestDto {

    @Getter @Setter
    public static class CreateCommentDto{
        String contents;
    }

    @Getter @Setter
    public static class UpdateCommentDto{
        String contents;
    }
}
