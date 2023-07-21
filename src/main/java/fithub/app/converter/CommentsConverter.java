package fithub.app.converter;

import fithub.app.domain.Comments;
import fithub.app.domain.User;
import fithub.app.repository.CommentsRepository.CommentsRepository;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
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

    public static Comments toCommentsArticle(CommentsRequestDto.CreateCommentDto request){
        return Comments.builder()
                .contents(request.getContents())
                .isRecord(false)
                .build();
    }

    public static Comments toCommentsRecord(CommentsRequestDto.CreateCommentDto request){
        return Comments.builder()
                .contents(request.getContents())
                .isRecord(true)
                .build();
    }
    public static CommentsResponseDto.CommentsDto toCommentsDto(Comments comments, User user) {
        return CommentsResponseDto.CommentsDto.builder()
                .commentId(comments.getId())
                .userInfo(UserConverter.toCommunityUserInfo(comments.getUser()))
                .contents(comments.getContents())
                .likes(comments.getLikes())
                .isLiked(user.isLikedCommentsFind(comments))
                .createdAt(comments.getCreatedAt())
                .build();
    }

    public static CommentsResponseDto.CommentsDtoList toCommentsDtoList(List<Comments> commentsList, User user){
        List<CommentsResponseDto.CommentsDto> commentsDtoList =
                commentsList.stream()
                        .map(comments -> toCommentsDto(comments, user))
                        .collect(Collectors.toList());

        return CommentsResponseDto.CommentsDtoList.builder()
                .commentList(commentsDtoList)
                .size(commentsDtoList.size())
                .build();

    }

    public static CommentsResponseDto.CreateCommentDto toCreateCommentDto(Comments comments){
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

    public static CommentsResponseDto.CommentLikeDto toCommentLikeDto(Comments comments){
        return CommentsResponseDto.CommentLikeDto.builder()
                .commentId(comments.getId())
                .newLikes(comments.getLikes())
                .build();
    }
}
