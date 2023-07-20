package fithub.app.repository.CommentsRepository;

import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {

    Page<Comments> findByCreatedAtLessThanAndArticleOrderByCreatedAtDesc(LocalDateTime createdAt, Article article, Pageable pageable);

    Page<Comments> findByArticleOrderByCreatedAtDesc(Article article, Pageable pageable);

    Page<Comments> findByCreatedAtLessThanAndRecordOrderByCreatedAtDesc(LocalDateTime createdAt, Record record, Pageable pageable);

    Page<Comments> findByRecordOrderByCreatedAtDesc(Record record, Pageable pageable);
    Optional<Comments> findByIdAndIsRecord(Long id, Boolean isRecord);

}
