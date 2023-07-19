package fithub.app.domain;

import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uuid_id")
    private Uuid uuid;

    public void setArticle(Article article){
        this.article = article;
        article.getArticleImageList().add(this);
    }
}
