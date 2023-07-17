package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
