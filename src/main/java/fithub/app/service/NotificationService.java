package fithub.app.service;

import fithub.app.domain.Notification;
import fithub.app.domain.User;
import org.springframework.data.domain.Page;

public interface NotificationService {

    Page<Notification> getNotification(User user, Integer integer);
}
