package fithub.app.converter;

import fithub.app.domain.Comments;
import fithub.app.domain.User;
import fithub.app.repository.CommentsRepository;
import fithub.app.web.dto.responseDto.CommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentsConverter {

    private final CommentsRepository commentsRepository;
    private static CommentsRepository staticCommentsRepository;

    @PostConstruct
    public void init() {
        staticCommentsRepository = this.commentsRepository;
    }

    public CommentsResponseDto.CommentsDto toCommentsDto(Comments comments) {
        return CommentsResponseDto.CommentsDto.builder()
                .commentId(comments.getId())
                .userInfo(UserConverter.toCommunityUserInfo(comments.getUser()))
                .contents(comments.getContents())
                .likes(comments.getLikes())
                .createdAt(comments.getCreatedAt())
                .build();
    }

    public CommentsResponseDto.CommentsDtoList toCommentsDtoList(List<Comments> commentsList){
        List<CommentsResponseDto.CommentsDto> commentsDtoList =
                commentsList.stream()
                        .map(comments -> toCommentsDto(comments))
                        .collect(Collectors.toList());

        return CommentsResponseDto.CommentsDtoList.builder()
                .commentList(commentsDtoList)
                .size(commentsDtoList.size())
                .build();

    }

    public CommentsResponseDto.CreateCommentDto toCreateCommentDto(Comments comments){
        return CommentsResponseDto.CreateCommentDto.builder()
                .commentId(comments.getId())
                .createdAt(comments.getCreatedAt())
                .build();
    }

    public CommentsResponseDto.UpdateCommentDto toUpdateCommentDto(Comments comments){
        return CommentsResponseDto.UpdateCommentDto.builder()
                .commentId(comments.getId())
                .updatedAt(comments.getUpdatedAt())
                .build();
    }

    public CommentsResponseDto.DeleteCommentDto toDeleteCommentDto(Long id){
        return CommentsResponseDto.DeleteCommentDto.builder()
                .commentId(id)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public CommentsResponseDto.CommentLikeDto toCommentLikeDto(Comments comments){
        return CommentsResponseDto.CommentLikeDto.builder()
                .commentId(comments.getId())
                .newLikes(comments.getLikes())
                .build();
    }
}
