package fithub.app.web.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationDto{
        String alarmType;
        String alarmBody;
        Long targetId;
        Long alarmId;
        Boolean isConfirmed;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationDtoList{
        List<NotificationDto> alarmList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NotificationConfirmDto{
        LocalDateTime confirmedAt;
    }
}
