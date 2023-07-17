package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.mapping.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

    Optional<SavedArticle> findByArticleAndUser(Article article, User user);
}
