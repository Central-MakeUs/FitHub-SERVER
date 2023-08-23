package fithub.app.firebase.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmMessageAppleV2 {

    private boolean validateOnly;
    private FcmMessageAppleV2.Message message;


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message{
        private FcmMessageAppleV2.Notification notification;
        private FcmMessageAppleV2.Data data;
        private String token;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Notification{
        private String title;
        private String body;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Data{
        private String targetView;
        private String targetPK;
        private String targetImage;
        private String targetNotification;
    }
}
