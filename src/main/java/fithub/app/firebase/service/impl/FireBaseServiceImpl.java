package fithub.app.firebase.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import fithub.app.firebase.dto.FcmMessageV1;
import fithub.app.firebase.dto.FcmMessageV2;
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
    public void sendMessageTo(String targetToken, String title, String body, String targetView, String targetPK) throws IOException {
        String message = makeMessage(targetToken, title, body, targetView, targetPK);

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

        logger.info("fire base 푸쉬알림 결과 : {}", response.code());
        logger.info("fire base 푸쉬알림 내용 : {}", message);
    }

    @Override
    public void sendMessageToV2(String targetToken, String title, String body, String targetView, String targetPK) throws IOException {
        String message = makeMessageV2(targetToken, title, body, targetView, targetPK);

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

        logger.info("fire base 푸쉬알림 결과 : {}", response.code());
        logger.info("fire base 푸쉬알림 내용 : {}", message);
    }




    private String makeMessage(String targeToken, String title, String body, String targetView, String targetPK) throws JsonParseException, JsonProcessingException{
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(
                        FcmMessage.Message.builder()
                                .token(targeToken).
                                data(FcmMessage.Data.builder()
                                        .title(title)
                                        .body(body)
                                        .targetView(targetView)
                                        .targetPK(targetPK).build()
                                ).
                                build()
                )
                .validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String makeMessageV2(String targeToken, String title, String body, String targetView, String targetPK) throws JsonParseException, JsonProcessingException{
        FcmMessageV1 fcmMessage = FcmMessageV1.builder()
                .message(
                        FcmMessageV1.Message.builder()
                                .token(targeToken).
                                data(FcmMessageV1.Data.builder()
                                        .title(title)
                                        .body(body)
                                        .targetView(targetView)
                                        .targetImage("https://cmc-fithub.s3.ap-northeast-2.amazonaws.com/fithub-record/b1df93fe-a86d-4e64-8d50-b4dc9c89c45b")
                                        .targetPK(targetPK).build()
                                ).
                                build()
                )
                .validateOnly(false).build();
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
