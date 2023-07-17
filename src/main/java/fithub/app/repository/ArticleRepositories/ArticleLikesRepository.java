package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ArticleLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {

    Optional<ArticleLikes> findByArticleAndUser(Article article, User user);
}
