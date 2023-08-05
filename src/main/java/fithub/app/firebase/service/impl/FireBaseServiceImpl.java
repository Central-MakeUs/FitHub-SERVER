package fithub.app.firebase.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import fithub.app.firebase.dto.FcmMessage;
import fithub.app.firebase.service.FireBaseService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FireBaseServiceImpl implements FireBaseService {

    @Value("${fcm.url}")
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/fithub-push-alarm/messages:send";
    private final ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(FireBaseServiceImpl.class);

    @Override
    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(
                message, MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        logger.info("fire base 푸쉬알림 결과 : {}", response.body().toString());
    }

    private String makeMessage(String targeToken, String title, String body) throws JsonParseException, JsonProcessingException{
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(
                        FcmMessage.Message.builder()
                                .token(targeToken)
                                .notification(
                                        FcmMessage.Notification.builder()
                                                .title(title)
                                                .body(body)
                                                .build()
                                ).build()
                ).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException{
        String fireBaseConfigPath = "firebase/fithub-firebase-key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(fireBaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
