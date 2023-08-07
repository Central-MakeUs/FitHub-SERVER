package fithub.app.web.dto.responseDto;

import lombok.*;

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
}
