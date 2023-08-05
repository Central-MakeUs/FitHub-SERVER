package fithub.app.firebase.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmMessageV2 {

    private boolean validateOnly;
    private Message message;


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message{
        private Notification notification;
        private String token;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Notification{
        private String title;
        private String body;
        private String targetView;
        private String targetPK;
    }
}
