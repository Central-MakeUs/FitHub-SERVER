package fithub.app.repository;

import fithub.app.domain.RecommendArticleKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendArticleKeywordRepository extends JpaRepository<RecommendArticleKeyword, Long> {
    List<RecommendArticleKeyword> findTop10ByOrderById();
}
