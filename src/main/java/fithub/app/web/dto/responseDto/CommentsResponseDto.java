package fithub.app.web.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CommentsResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentsDto{
        Long commentId;
        UserResponseDto.CommunityUserInfo userInfo;
        String contents;
        Long likes;
        Boolean isLiked;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentsDtoList{
        List<CommentsDto> commentList;
        Integer size;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateCommentDto{
        Long commentId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class UpdateCommentDto{
        Long commentId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteCommentDto{
        Long commentId;
        LocalDateTime deletedAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentLikeDto{
        Long commentId;
        Long newLikes;
        Boolean isLiked;
    }
}
