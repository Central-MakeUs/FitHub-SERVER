package fithub.app.repository;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentsReportRepository extends JpaRepository<ContentsReport, Long> {

    Optional<ContentsReport> findByUserAndArticle(User reporter, Article article);
}
