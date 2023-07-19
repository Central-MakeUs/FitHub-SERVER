package fithub.app.repository.ArticleRepositories;

import fithub.app.domain.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {

    Optional<ArticleImage> findByImageUrl(String imageUrl);
}
