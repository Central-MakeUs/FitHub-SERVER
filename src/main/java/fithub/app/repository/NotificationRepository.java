package fithub.app.repository;

import fithub.app.domain.Notification;
import fithub.app.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query("select count (n) from Notification n where n.user = :user and n.isConfirmed = false")
    Long findRemainAlarm(@Param("user") User user);

    @Query("select n from Notification n where n.isConfirmed = true")
    List<Notification> findTargetNotification();
}
