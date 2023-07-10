package fithub.app.sms.dto;

import lombok.*;

import java.time.LocalDateTime;


public class SmsDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PhoneAuthDto{
        String phoneNum;
        LocalDateTime sendTime;
        Integer authNum;
    }
}
