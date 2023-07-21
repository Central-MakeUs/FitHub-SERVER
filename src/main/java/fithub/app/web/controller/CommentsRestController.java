package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.CommentsConverter;
import fithub.app.domain.Comments;
import fithub.app.domain.User;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentsRestController {

    private final CommentsService commentsService;

    Logger logger = LoggerFactory.getLogger(CommentsRestController.class);

    @Operation(summary = "댓글 조회 API ✔️", description = "댓글 조회 API 입니다. 게시글/운동 인증을 type으로 구분하며 상세 조회 시 이 API 까지 2개 조회!, last로 페이징도 있습니다.")
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
            @Parameter(name = "last", description = "한번의 스크롤의 마지막으로 조회 된 댓글의 아이디, 페이징을 위함"),
            @Parameter(name = "id", description = "게시글/운동 인증의 아이디")
    })
    @GetMapping("/{type}/{id}/comments")
    public ResponseDto<CommentsResponseDto.CommentsDtoList>articleCommentList(@RequestParam(name = "last", required = false) Long last,@PathVariable(name = "type") String type,@PathVariable(name = "id") @ExistArticle Long id, @AuthUser User user){
        Page<Comments> comments = type.equals("articles") ? commentsService.findOnArticle(id, last) : commentsService.findOnRecord(id, last);
        return ResponseDto.of(CommentsConverter.toCommentsDtoList(comments.toList(), user));
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
    public ResponseDto<CommentsResponseDto.CreateCommentDto> createCommentArticle(@PathVariable(name = "type") String type,@PathVariable(name = "id") Long id,@RequestBody CommentsRequestDto.CreateCommentDto request, @AuthUser User user){
        Comments newComments = type.equals("articles") ? commentsService.createOnArticle(request,id, user) : commentsService.createOnRecord(request, id, user);
        return ResponseDto.of(CommentsConverter.toCreateCommentDto(newComments));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글 수정 API 입니다.")
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
    public ResponseDto<CommentsResponseDto.UpdateCommentDto> updateCommentArticle(@PathVariable(name = "type") String type,@PathVariable(name = "id") Long id, @PathVariable(name = "commentId") Long commentId, @RequestBody CommentsRequestDto.UpdateCommentDto request, @AuthUser User user){
        return null;
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API 입니다.")
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
        return null;
    }
    @Operation(summary = "댓글 좋아요 누르기/취소 API", description = "댓글 좋아요 누르기/취소 API 입니다.")
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
        return ResponseDto.of(CommentsConverter.toCommentLikeDto(comments));
    }
}
