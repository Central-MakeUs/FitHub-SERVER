package fithub.app.repository.HashTagRepositories;

import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.mapping.ArticleHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleHashTagRepository extends JpaRepository<ArticleHashTag, Long> {

    List<ArticleHashTag> findAllByHashTag(HashTag hashTag);
}
