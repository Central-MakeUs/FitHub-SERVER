package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.NotificationException;
import fithub.app.domain.Notification;
import fithub.app.domain.User;
import fithub.app.repository.NotificationRepository;
import fithub.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationServerImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Page<Notification> getNotification(User user, Integer pageIndex) {

        if(pageIndex == null)
            pageIndex = 0;

        return notificationRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(pageIndex, 12));
    }

    @Override
    @Transactional
    public void confirmNotification(Long alarmId, User user) {
        Notification notification = notificationRepository.findById(alarmId).orElseThrow(() -> new NotificationException(Code.ARTICLE_NOT_FOUND));
        notification.setIsConfirmed();
    }
}
