package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.service.converter.NotificationConverter;

import fithub.app.domain.Notification;
import fithub.app.domain.User;
import fithub.app.firebase.service.FireBaseService;
import fithub.app.service.NotificationService;
import fithub.app.web.dto.responseDto.NotificationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "알람 API", description = "푸쉬 알람 API")
@RestController
@RequiredArgsConstructor
public class AlarmRestController {

    private final FireBaseService fireBaseService;

    private final NotificationService notificationService;

    @Operation(summary = "내 알림목록 조회 API ✔️🔑", description = "pageIndex로 페이징")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/alarms")
    public ResponseDto<NotificationResponseDto.NotificationDtoList> showAlarm(@AuthUser User user, @RequestParam(name = "pageIndex") Integer pageIndex) {
        Page<Notification> notification = notificationService.getNotification(user, pageIndex);
        return ResponseDto.of(NotificationConverter.toNotificationDtoList(notification));
    }

    @Operation(summary = "내 알림 확인 API ✔️🔑", description = "내 알림 확인 API 입니다. 확인 한 알림은 추후 알림 목록에서 다르게 표시가 됩니다")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4071", description = "BAD_REQUEST : 알림이 없음", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요", content = @Content(schema = @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/alarms/{alarmId}")
    public ResponseDto<NotificationResponseDto.NotificationConfirmDto> confirmNotification(@PathVariable(name = "alarmId") Long alarmId, @AuthUser User user) {
        notificationService.confirmNotification(alarmId, user);
        return ResponseDto.of(NotificationConverter.toNotificationConfirmDto());
    }
}
