package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.CommentsConverter;
import fithub.app.domain.Comments;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.service.CommentsService;
import fithub.app.validation.annotation.ExistArticle;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
import fithub.app.web.dto.responseDto.CommentsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentsRestController {

    private final CommentsService commentsService;

    Logger logger = LoggerFactory.getLogger(CommentsRestController.class);

    @Operation(summary = "댓글 조회 API ✔️", description = "댓글 조회 API 입니다. 게시글/운동 인증을 type으로 구분하며 상세 조회 시 이 API 까지 2개 조회!, categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 존재하지 않음, 없는 게시글의 댓글 조회 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 존재하지 않음, 없는 운동 인증의 댓글 조회 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "BAD_REQUEST : url에 type을 확인해주세요", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "type", description = "articles면 게시글의 댓글, records면 운동 인증의 댓글"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디")
    })
    @GetMapping("/{type}/{id}/comments")
    public ResponseDto<CommentsResponseDto.CommentsDtoList>articleCommentList(@RequestParam(name = "pageIndex") Integer pageIndex,@PathVariable(name = "type") String type,@PathVariable(name = "id") @ExistArticle Long id, @AuthUser User user){
        Page<Comments> comments = type.equals("articles") ? commentsService.findOnArticle(id, pageIndex, user) : commentsService.findOnRecord(id, pageIndex,user);
        return ResponseDto.of(CommentsConverter.toCommentsDtoList(comments, user));
    }

    @Operation(summary = "댓글 작성 API ✔️", description = "댓글 작성 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 존재하지 않음, 없는 게시글의 댓글 작성 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 존재하지 않음, 없는 운동 인증의 댓글 작성 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "BAD_REQUEST : url에 type을 확인해주세요", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "type", description = "articles면 게시글의 댓글, records면 운동 인증의 댓글"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디")
    })
    @PostMapping("/{type}/{id}/comments")
    public ResponseDto<CommentsResponseDto.CreateCommentDto> createCommentArticle(@PathVariable(name = "type") String type,@PathVariable(name = "id") Long id,@RequestBody CommentsRequestDto.CreateCommentDto request, @AuthUser User user) throws IOException
    {
        Comments newComments = type.equals("articles") ? commentsService.createOnArticle(request,id, user) : commentsService.createOnRecord(request, id, user);
        if(type.equals("articles")) {
            if(!newComments.getUser().getId().equals(newComments.getArticle().getUser().getId()))
                commentsService.commentAlarmArticle(newComments.getArticle(), newComments, user, newComments.getArticle().getUser());
        }
        else {
            if(!newComments.getUser().getId().equals(newComments.getRecord().getUser().getId()))
                commentsService.commentAlarmRecord(newComments.getRecord(), newComments, user);
        }
        return ResponseDto.of(CommentsConverter.toCreateCommentDto(newComments));
    }

    @Operation(summary = "댓글 수정 API ✔️", description = "댓글 수정 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 존재하지 않음, 없는 게시글의 댓글 수정 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 존재하지 않음, 없는 운동 인증의 댓글 수정 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4051", description = "NOT_FOUND : 댓글이 존재하지 않음", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4052", description = "FORBIDDEN : 다른 사람의 댓글.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "BAD_REQUEST : url에 type을 확인해주세요", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "type", description = "articles면 게시글의 댓글, records면 운동 인증의 댓글"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디"),
            @Parameter(name = "commentId", description = "댓글의 아이디"),
    })
    @PatchMapping("/{type}/{id}/comments/{commentId}")
    public ResponseDto<CommentsResponseDto.UpdateCommentDto> updateCommentArticle(@PathVariable(name = "type") String type, @PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId, @RequestBody @Valid CommentsRequestDto.UpdateCommentDto request, @AuthUser User user){
        Comments updatedComments = type.equals("articles") ? commentsService.updateOnArticle(request, id, commentId, user) : commentsService.updateOnRecord(request, id, commentId, user);
        return ResponseDto.of(CommentsConverter.toUpdateCommentDto(updatedComments));
    }

    @Operation(summary = "댓글 삭제 API ✔️", description = "댓글 삭제 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 존재하지 않음, 없는 게시글의 댓글 삭제 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 존재하지 않음, 없는 운동 인증의 댓글 삭제 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4051", description = "NOT_FOUND : 댓글이 존재하지 않음", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4052", description = "FORBIDDEN : 다른 사람의 댓글.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "BAD_REQUEST : url에 type을 확인해주세요", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "type", description = "articles면 게시글의 댓글, records면 운동 인증의 댓글"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디"),
            @Parameter(name = "commentId", description = "댓글의 아이디"),
    })
    @DeleteMapping("/{type}/{id}/comments/{commentId}")
    public ResponseDto<CommentsResponseDto.DeleteCommentDto> deleteComment(@PathVariable(name = "type") String type,@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId,@AuthUser User user){
        if (type.equals("articles"))
            commentsService.deleteOnArticle(id, commentId, user);
        else
            commentsService.deleteOnRecord(id, commentId, user);
        return ResponseDto.of(CommentsConverter.toDeleteCommentDto(commentId));
    }
    @Operation(summary = "댓글 좋아요 누르기/취소 API ✔️", description = "댓글 좋아요 누르기/취소 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 존재하지 않음, 없는 게시글의 댓글 좋아요/취소 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 존재하지 않음, 없는 운동 인증의 댓글 좋아요/취소 시도.", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4051", description = "NOT_FOUND : 댓글이 존재하지 않음", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "BAD_REQUEST : url에 type을 확인해주세요", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4053", description = "FORBIDDEN : 자신의 댓글은 좋아요를 누를 수 없음", content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "type", description = "articles면 게시글의 댓글, records면 운동 인증의 댓글"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디"),
            @Parameter(name = "commentId", description = "댓글의 아이디"),
    })
    @Transactional(readOnly = false)
    @PostMapping("/{type}/{id}/comments/{commentId}")
    public ResponseDto<CommentsResponseDto.CommentLikeDto> toggleComment(@PathVariable(name = "type") String type,@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId,@AuthUser User user){
        Comments comments = type.equals("articles") ? commentsService.toggleCommentsLikeOnArticle(id, commentId, user) : commentsService.toggleCommentsLikeOnRecord(id, commentId, user);
        return ResponseDto.of(CommentsConverter.toCommentLikeDto(comments,user));
    }

    @Operation(summary = "댓글 신고하기 ✔️🔑",description = "댓글을 신고하는 API이며 이미 신고한 경우는 안된다고 응답이 갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4051", description = "NOT_FOUND : 댓글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4061", description = "BAD_REQUEST : 이미 신고 했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4062", description = "BAD_REQUEST : 자신의 콘텐츠는 신고가 안됩니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "commentsId", description = "댓글 아이디")
    })
    @PostMapping("/comments/{commentsId}/report")
    public ResponseDto<CommentsResponseDto.CommentsReportDto> reportComments(@PathVariable(name = "commentsId") Long commentsId, @AuthUser User user){
        ContentsReport contentsReport = commentsService.reportComments(commentsId, user);
        return ResponseDto.of(CommentsConverter.toCommentsReportDto(commentsId, contentsReport));
    }
}
