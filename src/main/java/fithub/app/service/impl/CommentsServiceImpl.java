package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.CommentsException;
import fithub.app.converter.CommentsConverter;
import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.CommentsLikes;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.CommentsRepository.CommentsLikesRepository;
import fithub.app.repository.CommentsRepository.CommentsRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.service.CommentsService;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final ArticleRepository articleRepository;
    private final RecordRepository recordRepository;

    private final CommentsLikesRepository commentsLikesRepository;

    Logger logger = LoggerFactory.getLogger(CommentsServiceImpl.class);

    @Value("${paging.comments.size}")
    Integer size;

    @Override
    public Page<Comments> findOnArticle(Long id, Long last) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));

        Page<Comments> comments = null;

        if(last == null)
            last = 0L;
        Optional<Comments> lastComments = commentsRepository.findByIdAndIsRecord(last, false);
        if(lastComments.isPresent())
            comments = commentsRepository.findByCreatedAtLessThanAndArticleOrderByCreatedAtDesc(lastComments.get().getCreatedAt(), article, PageRequest.of(0, size));
        else
            comments = commentsRepository.findByArticleOrderByCreatedAtDesc(article, PageRequest.of(0, size));
        return comments;
    }

    @Override
    public Page<Comments> findOnRecord(Long id, Long last) {
        Record record = recordRepository.findById(id).orElseThrow(() -> new CommentsException(Code.RECORD_NOT_FOUND));

        Page<Comments> comments = null;

        if(last == null)
            last = 0L;
        Optional<Comments> lastComments = commentsRepository.findByIdAndIsRecord(last, true);
        if(lastComments.isPresent())
            comments = commentsRepository.findByCreatedAtLessThanAndRecordOrderByCreatedAtDesc(lastComments.get().getCreatedAt(), record, PageRequest.of(0, size));
        else
            comments = commentsRepository.findByRecordOrderByCreatedAtDesc(record, PageRequest.of(0, size));
        return comments;
    }

    @Override
    @Transactional(readOnly = false)
    public Comments createOnArticle(CommentsRequestDto.CreateCommentDto request, Long id, User user) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = CommentsConverter.toCommentsArticle(request);
        comments.setUser(user);
        comments.setArticle(article);
        article.countComments();
        return commentsRepository.save(comments);
    }

    @Override
    @Transactional(readOnly = false)
    public Comments createOnRecord(CommentsRequestDto.CreateCommentDto request, Long id, User user) {
        Record record = recordRepository.findById(id).orElseThrow(()->new CommentsException(Code.RECORD_NOT_FOUND));
        Comments comments = CommentsConverter.toCommentsRecord(request);
        comments.setRecord(record);
        comments.setUser(user);
        record.countComments();
        return commentsRepository.save(comments);
    }

    @Override
    public Comments updateOnArticle(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        return null;
    }

    @Override
    public Comments updateOnRecord(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        return null;
    }

    @Override
    public void deleteOnArticle(Long id, Long commentsId, User user) {

    }

    @Override
    public void deleteOnRecord(Long id, Long commentsId, User user) {

    }

    @Override
    public Comments toggleCommentsLikeOnArticle(Long id, Long commentsId, User user) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        Optional<CommentsLikes> commentsLikesOptional = commentsLikesRepository.findByCommentsAndUser(comments, user);
        if (comments.getUser().equals(user))
            throw new CommentsException(Code.COMMENTS_LIKES_FORBBIDDEN);
        if(commentsLikesOptional.isPresent())
        {
            CommentsLikes commentsLikes = commentsLikesOptional.get();
            user.getCommentsLikesList().remove(commentsLikes);
            comments.getCommentsLikesList().remove(commentsLikes);
            commentsLikesRepository.delete(commentsLikes);
            comments.toggleLikes(true);
        }
        else{
            CommentsLikes newCommentsLikes = CommentsLikes.builder()
                    .comments(comments)
                    .user(user)
                    .build();
            user.getCommentsLikesList().add(newCommentsLikes);
            newCommentsLikes.setComments(comments);
            newCommentsLikes.setUser(user);
            commentsLikesRepository.save(newCommentsLikes);
            comments.toggleLikes(false);
        }
        return comments;
    }

    @Override
    public Comments toggleCommentsLikeOnRecord(Long id, Long commentsId, User user) {
        return null;
    }
}