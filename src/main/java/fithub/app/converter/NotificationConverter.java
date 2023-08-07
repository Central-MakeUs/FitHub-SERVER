package fithub.app.converter;

import fithub.app.domain.Notification;
import fithub.app.domain.enums.NotificationCategory;
import fithub.app.utils.FCMType;
import fithub.app.web.dto.responseDto.NotificationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationConverter {

    public static NotificationResponseDto.NotificationDto toNotificationDto(Notification notification){

        Long targetId = notification.getNotificationCategory().equals(NotificationCategory.ARTICLE) ? notification.getArticle().getId() : notification.getRecord().getId();

        return NotificationResponseDto.NotificationDto.builder()
                .alarmType(notification.getNotificationCategory().toString())
                .alarmBody(notification.getNotificationBody())
                .isConfirmed(notification.getIsConfirmed())
                .alarmId(notification.getId())
                .createdAt(notification.getCreatedAt())
                .targetId(targetId)
                .build();

    }

    public static NotificationResponseDto.NotificationDtoList toNotificationDtoList(Page<Notification> notificationList){
        List<NotificationResponseDto.NotificationDto> notificationDtoList = notificationList.stream()
                .map(notification -> toNotificationDto(notification)).collect(Collectors.toList());

        return NotificationResponseDto.NotificationDtoList.builder()
                .alarmList(notificationDtoList)
                .isFirst(notificationList.isFirst())
                .isLast(notificationList.isLast())
                .listSize(notificationList.getSize())
                .totalElements(notificationList.getTotalElements())
                .totalPage(notificationList.getTotalPages())
                .build();
    }

    public static NotificationResponseDto.NotificationConfirmDto toNotificationConfirmDto(){
        return NotificationResponseDto.NotificationConfirmDto.builder()
                .confirmedAt(LocalDateTime.now())
                .build();
    }
}
