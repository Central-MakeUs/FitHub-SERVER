package fithub.app.repository;

import fithub.app.domain.Article;
import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentsReportRepository extends JpaRepository<ContentsReport, Long> {

    Optional<ContentsReport> findByUserAndArticle(User reporter, Article article);

    Optional<ContentsReport> findByUserAndRecord(User reporter, Record record);

    Optional<ContentsReport> findByUserAndComments(User reporter, Comments comments);
}
