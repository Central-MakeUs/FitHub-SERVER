package fithub.app.sms.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageDto {
    String to;
    String content;
}
