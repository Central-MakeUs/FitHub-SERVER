package fithub.app.domain.mapping;

import fithub.app.domain.Article;
import fithub.app.domain.User;
import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavedArticle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    public void setUser(User user){
        if (this.user != null) {
            this.user.getSavedArticleList().remove(this);
        }
        this.user = user;
        user.getSavedArticleList().add(this);
    }
}
