package fithub.app.domain.mapping;


import fithub.app.domain.Comments;
import fithub.app.domain.User;
import fithub.app.domain.common.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsLikes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_comment")
    private Comments comments;

    public void setUser(User user){
        if (this.user != null)
            user.getCommentsLikesList().remove(this);
        this.user = user;
        user.getCommentsLikesList().add(this);
    }

    public void setComments(Comments comments){
        if (this.comments != null)
            comments.getCommentsLikesList().remove(this);
        this.comments = comments;
        comments.getCommentsLikesList().add(this);
    }
}
