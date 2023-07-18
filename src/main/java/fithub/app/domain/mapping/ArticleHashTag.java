package fithub.app.domain.mapping;


import fithub.app.domain.Article;
import fithub.app.domain.HashTag;
import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleHashTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public void setArticle(Article article){
        if (this.article != null) {
            this.article.getArticleHashTagList().remove(this);
        }
        this.article = article;
        article.getArticleHashTagList().add(this);
    }

    public void setHashTag(HashTag hashTag){
        if (this.hashTag != null) {
            this.hashTag.getArticleHashTagList().remove(this);
        }
        this.hashTag = hashTag;
        hashTag.getArticleHashTagList().add(this);
    }
}
