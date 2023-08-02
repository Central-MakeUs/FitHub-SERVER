package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.base.exception.handler.CommentsException;
import fithub.app.converter.CommentsConverter;
import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.enums.ContentsType;
import fithub.app.domain.mapping.CommentsLikes;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.CommentsRepository.CommentsLikesRepository;
import fithub.app.repository.CommentsRepository.CommentsRepository;
import fithub.app.repository.ContentsReportRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.repository.UserRepository;
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

    private final ContentsReportRepository contentsReportRepository;

    private final UserRepository userRepository;

    @Value("${paging.comments.size}")
    Integer size;

    @Override
    public Page<Comments> findOnArticle(Long id, Integer pageIndex) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));

        Page<Comments> comments = null;

        if(pageIndex == null)
            pageIndex = 0;
        comments = commentsRepository.findByArticleOrderByCreatedAtDesc(article, PageRequest.of(pageIndex, size));
        return comments;
    }

    @Override
    public Page<Comments> findOnRecord(Long id, Integer pageIndex) {
        Record record = recordRepository.findById(id).orElseThrow(() -> new CommentsException(Code.RECORD_NOT_FOUND));

        Page<Comments> comments = null;

        if(pageIndex == null)
            pageIndex = 0;
        comments = commentsRepository.findByRecordOrderByCreatedAtDesc(record, PageRequest.of(pageIndex, size));
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
    @Transactional(readOnly = false)
    public Comments updateOnArticle(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        articleRepository.findById(id).orElseThrow(() -> new ArticleException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        Comments updatedComments = comments.setContents(request.getContents());
        return updatedComments;
    }

    @Override
    @Transactional(readOnly = false)
    public Comments updateOnRecord(CommentsRequestDto.UpdateCommentDto request, Long id, Long commentsId, User user) {
        recordRepository.findById(id).orElseThrow(() -> new ArticleException(Code.RECORD_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        Comments updatedComments = comments.setContents(request.getContents());
        return updatedComments;
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOnArticle(Long id, Long commentsId, User user) {
        articleRepository.findById(id).orElseThrow(()->new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        comments.getArticle().deleteComments();
        commentsRepository.delete(comments);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteOnRecord(Long id, Long commentsId, User user) {
        recordRepository.findById(id).orElseThrow(()->new CommentsException(Code.RECORD_NOT_FOUND));
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(() -> new CommentsException(Code.COMMENT_NOT_FOUND));
        if (!comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.COMMENTS_FORBIDDEN);
        comments.getRecord().deleteComments();
        commentsRepository.delete(comments);
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
        Record record= recordRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
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
    @Transactional
    public ContentsReport reportComments(Long commentsId, User user) {
        Comments comments = commentsRepository.findById(commentsId).orElseThrow(()->new CommentsException(Code.COMMENT_NOT_FOUND));
        if(comments.getUser().getId().equals(user.getId()))
            throw new CommentsException(Code.MY_CONTENTS);
        Optional<ContentsReport> findReport = contentsReportRepository.findByUserAndComments(user, comments);
        if(findReport.isPresent())
            throw new CommentsException(Code.ALREADY_REPORT);
        User commentsOwner = userRepository.findById(comments.getUser().getId()).get();
        commentsOwner.countReport();
        comments.countReport();
        return contentsReportRepository.save(ContentsReport.builder()
                .contentsType(ContentsType.COMMENT)
                .comments(comments)
                .user(user)
                .build()
        );
    }
}
