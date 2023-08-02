package fithub.app.domain;

import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.ArticleLikes;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseEntity {

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", comments=" + comments +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", views=" + views +
                ", reported=" + reported +
                ", saves=" + saves +
                ", likes=" + likes +
                ", articleImageList=" + articleImageList +
                ", articleHashTagList=" + articleHashTagList +
                ", commentsList=" + commentsList +
                ", user=" + user +
                ", exerciseCategory=" + exerciseCategory +
                '}';
    }

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

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleLikes> articleLikesList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private ExerciseCategory exerciseCategory;

    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    private List<ContentsReport> reportList;

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

    public Article saveToggle(Boolean flag){
        if (flag)
            this.saves += 1;
        else
            this.saves -= 1;

        return this;
    }

    public void update(ArticleRequestDto.UpdateArticleDto request, ExerciseCategory exerciseCategory){
        this.title = request.getTitle();
        this.contents = request.getContents();
        this.exerciseCategory = exerciseCategory;
    }

    public void countComments(){
        this.comments += 1;
    }

    public void deleteComments(){this.comments -= 1;}

    public void countReport(){this.reported += 1;}
}
