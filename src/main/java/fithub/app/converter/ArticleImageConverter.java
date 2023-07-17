package fithub.app.converter;

import fithub.app.domain.Article;
import fithub.app.domain.ArticleImage;
import fithub.app.domain.Uuid;
import fithub.app.repository.ArticleImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ArticleImageConverter {

    private final ArticleImageRepository articleImageRepository;

    private static ArticleImageRepository staticArticleImageRepository;

    @PostConstruct
    public void init() {
        staticArticleImageRepository = this.articleImageRepository;
    }

    public static ArticleImage toArticleImage(String fileUrl, Article article, Uuid uuid){
        ArticleImage articleImage = ArticleImage.builder()
                .imageUrl(fileUrl)
                .article(article)
                .uuid(uuid)
                .build();
        return staticArticleImageRepository.save(articleImage);
    }
}
