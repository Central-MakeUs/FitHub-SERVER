package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
import fithub.app.web.dto.responseDto.CommentsResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentsRestController {

    @GetMapping("/article/{id}/comments")
    public ResponseEntity<CommentsResponseDto.CommentsDtoList> articleCommentList(@PathVariable Long id, @RequestParam Long last){
        return null;
    }


    @PostMapping("/article/{id}/comment")
    public ResponseEntity<CommentsResponseDto.CreateCommentDto> createCommentArticle(@PathVariable Long id,@RequestBody CommentsRequestDto.CreateCommentDto request, @AuthUser User user){
        return null;
    }

    @PatchMapping("/article/{id}/comment/{commentId}")
    public ResponseEntity<CommentsResponseDto.UpdateCommentDto> updateCommentArticle(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentsRequestDto.UpdateCommentDto request, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/article/{id}/comment/{commentId}")
    public ResponseEntity<CommentsResponseDto.DeleteCommentDto> deleteComment(@PathVariable Long id,@PathVariable Long commentId,@AuthUser User user){
        return null;
    }

    @GetMapping("/record/{id}/comments")
    public ResponseEntity<CommentsResponseDto.CommentsDtoList> recordCommentList(@PathVariable Long id){
        return null;
    }

    @PostMapping("/record/{id}/comment")
    public ResponseEntity<CommentsResponseDto.CreateCommentDto> createCommentRecord(@PathVariable Long id, @RequestBody CommentsRequestDto.CreateCommentDto request, @AuthUser User user){
        return null;
    }

    @PatchMapping("/record/{id}/comment/{commentId}")
    public ResponseEntity<CommentsResponseDto.UpdateCommentDto> updateCommentRecord(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentsRequestDto.UpdateCommentDto request, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/record/{id}/comment/{commentId}")
    public ResponseEntity<CommentsResponseDto.DeleteCommentDto> deleteCommentRecord(@PathVariable Long id,@PathVariable Long commentId,@AuthUser User user){
        return null;
    }
}
