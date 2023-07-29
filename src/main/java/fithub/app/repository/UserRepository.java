package fithub.app.repository;

import fithub.app.domain.User;
import fithub.app.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndSocialType(String socialId,SocialType socialType);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPhoneNum(String PhoneNum);

    List<User> findTop5ByOrderByTotalRecordNumDesc();
}
