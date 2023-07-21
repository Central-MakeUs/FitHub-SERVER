package fithub.app.repository.CommentsRepository;

import fithub.app.domain.Comments;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.CommentsLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentsLikesRepository extends JpaRepository<CommentsLikes, Long> {
    Optional<CommentsLikes> findByCommentsAndUser(Comments comments, User user);
}
