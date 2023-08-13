package fithub.app.repository;

import fithub.app.domain.RecommendFacilitiesKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendFacilitiesKeywordRepository extends JpaRepository<RecommendFacilitiesKeyword, Long> {

    List<RecommendFacilitiesKeyword> findTop10ByOrderById();
}
