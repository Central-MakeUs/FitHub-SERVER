package fithub.app.domain;

import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.mapping.ArticleHashTag;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long comments;

    @Column(columnDefinition = "VARCHAR(50)")
    private String title;

    private String contents;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long views;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reported;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long saves;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleImage> articleImageList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleHashTag> articleHashTagList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private ExerciseCategory exerciseCategory;

    public void setUser(User user){
        this.user = user;
        user.getArticleList().add(this);
    }

    public void setArticleHashTagList(List<ArticleHashTag> articleHashTagList){
        this.articleHashTagList = articleHashTagList;
    }

    public Article likeToggle(Boolean flag){
        if (flag)
            this.likes += 1;
        else
            this.likes -= 1;

        return this;
    }
}
