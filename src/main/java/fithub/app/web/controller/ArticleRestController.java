package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleRestController {

    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponseDto.ArticleDto> articleSpec(@PathVariable Long id){
        return null;
    }

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleTimeList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/articles/likes")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleLikesList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/articles/{categoryId}")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleCategoryTimeList(@PathVariable Long categoryId){
        return  null;
    }

    @GetMapping("/articles/{category}/likes")
    public ResponseEntity<ArticleResponseDto.ArticleDtoList> articleCategoryLikesList(@PathVariable Long category){
        return null;
    }

    @PostMapping("/article")
    public ResponseEntity<ArticleResponseDto.ArticleCreateDto> createArticle(@RequestBody ArticleRequestDto.createArticleDto request, @AuthUser User user){
        return null;
    }

    @PatchMapping("/article/{id}")
    public ResponseEntity<ArticleResponseDto.ArticleUpdateDto> updateArticle(@PathVariable Long id ,@RequestBody ArticleRequestDto.updateArticleDto request, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<ArticleResponseDto.ArticleDeleteDto> deleteArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/articles")
    public ResponseEntity<ArticleResponseDto.ArticleDeleteDtoList> deleteListArticle(@RequestBody ArticleRequestDto.deleteListArticleDto request, @AuthUser User user){
        return null;
    }

    @PostMapping("/article/{id}/likes")
    public ResponseEntity<ArticleResponseDto.ArticleLikeDto> likeArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @PostMapping("/article/{id}/scrap")
    public ResponseEntity<ArticleResponseDto.ArticleScrapDto> scrapArticle(@PathVariable Long id, @AuthUser User user){
        return null;
    }
}
