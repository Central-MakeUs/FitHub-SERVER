package fithub.app.domain;


import fithub.app.domain.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserExercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer exp;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Long records;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer contiguousDay;

    private LocalDate recentRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private ExerciseCategory exerciseCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade")
    private Grade grade;

    public void setUser(User user){
        if(this.user != null)
            user.getUserExerciseList().remove(this);
        this.user = user;
        user.getUserExerciseList().add(this);
    }

    public void setGrade(Grade grade){
        this.grade = grade;
    }
}
