package fithub.app.domain;


import com.amazonaws.services.ec2.model.LocalGateway;
import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.mapping.CommentsLikes;
import fithub.app.domain.mapping.ContentsReport;
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
public class Comments extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;

    @Column(columnDefinition = "VARCHAR(100)")
    private String contents;

    private Boolean isRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment")
    private Comments parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reported;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    List<CommentsLikes> commentsLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    List<ContentsReport> contentsReportList = new ArrayList<>();

    public void setUser(User user){
        if(this.user != null)
            user.getCommentsList().remove(this);
        this.user = user;
        user.getCommentsList().add(this);
    }

    public void setArticle(Article article){
        if(this.article != null)
            article.getCommentsList().remove(this);
        this.article = article;
        article.getCommentsList().add(this);
    }

    public void setRecord(Record record){
        if(this.record != null)
            record.getCommentsList().remove(this);
        this.record = record;
        record.getCommentsList().add(this);
    }

    public void toggleLikes(Boolean flag){
        this.likes  = flag ? this.likes - 1 : this.likes + 1;
    }

    public Comments setContents(String contents){this.contents = contents; return this;}

    public void countReport(){this.reported += 1;}
}
