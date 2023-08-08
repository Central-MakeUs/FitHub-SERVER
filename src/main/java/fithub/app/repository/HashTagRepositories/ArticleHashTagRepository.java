package fithub.app.repository.HashTagRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ArticleHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleHashTagRepository extends JpaRepository<ArticleHashTag, Long> {

    @Query("select ah from ArticleHashTag ah where ah.hashTag = :hashTag and ah.article.user not in (select ur.user from UserReport ur where ur.reporter = :reporter) and ah.article.user not in (select ur.reporter from UserReport  ur where ur.user = :target)")
    List<ArticleHashTag> findAllByHashTag(@Param("hashTag") HashTag hashTag, @Param("reporter")User reporter, @Param("target") User target);
}
