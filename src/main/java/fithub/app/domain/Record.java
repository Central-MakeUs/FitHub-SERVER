package fithub.app.domain;


import fithub.app.domain.common.BaseEntity;
import fithub.app.domain.mapping.RecordHashTag;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(30)")
    private String contents;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;

    private String imageUrl;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long comments;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long dailyLikes;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long weeklyLikes;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long reported;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private ExerciseCategory exerciseCategory;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
    private List<RecordHashTag> recordHashTagList = new ArrayList<>();

    public Record setImage(String imageUrl){
        this.imageUrl = imageUrl;
        return this;
    }

    public void setRecordHashTagList(List<RecordHashTag> recordHashTagList){
        this.recordHashTagList = recordHashTagList;
    }

    public void setUser(User user){
        if (this.user != null) {
            this.user.getRecordList().remove(this);
        }
        this.user = user;
        user.getRecordList().add(this);
    }

    public Record likeToggle(Boolean flag){
        if (flag)
            this.likes += 1;
        else
            this.likes -= 1;

        return this;
    }
}
