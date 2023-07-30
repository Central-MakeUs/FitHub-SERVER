package fithub.app.repository;

import fithub.app.domain.User;
import fithub.app.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialIdAndSocialType(String socialId,SocialType socialType);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByPhoneNum(String PhoneNum);

    List<User> findTop5ByOrderByTotalRecordNumDesc();

    @Modifying
    @Query("UPDATE User u SET u.monthlyRecordNum = 0 WHERE u.id = :id")
    void resetMonthlyRecordForUsers(@Param("id") Long id);
}
