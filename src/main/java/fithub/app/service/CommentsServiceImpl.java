package fithub.app.service;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.CommentsException;
import fithub.app.converter.CommentsConverter;
import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.repository.ArticleRepositories.ArticleRepository;
import fithub.app.repository.CommentsRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.web.dto.requestDto.CommentsRequestDto;
import fithub.app.web.dto.responseDto.CommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final ArticleRepository articleRepository;
    private final RecordRepository recordRepository;

    Logger logger = LoggerFactory.getLogger(CommentsServiceImpl.class);

    @Override
    @Transactional(readOnly = false)
    public Comments createOnArticle(CommentsRequestDto.CreateCommentDto request, Long id, User user) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CommentsException(Code.ARTICLE_NOT_FOUND));
        Comments comments = CommentsConverter.toCommentsArticle(request);
        comments.setUser(user);
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
        return null;
    }

    @Override
    public Comments toggleCommentsLikeOnRecord(Long id, Long commentsId, User user) {
        return null;
    }
}
