package fithub.app.domain.mapping;

import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordLikes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public void setUser(User user){
        if(this.user != null)
            user.getRecordLikesList().remove(this);
        this.user = user;
        user.getRecordLikesList().add(this);
    }

}
