package fithub.app.sms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fithub.app.converter.PhoneAuthConverter;
import fithub.app.domain.PhoneAuth;
import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.PhoneAuthException;
import fithub.app.repository.PhoneAuthRepository;
import fithub.app.sms.dto.MessageDto;
import fithub.app.sms.dto.SmsDto;
import fithub.app.sms.dto.SmsRequestDto;
import fithub.app.sms.dto.SmsResponseDto;
import fithub.app.sms.service.SmsService;
import fithub.app.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmsServiceImpl implements SmsService {

    private final PhoneAuthRepository phoneAuthRepository;

    @Value("${naver-sms.accessKey}")
    private String accessKey;

    @Value("${naver-sms.secretKey}")
    private String secretKey;

    @Value("${naver-sms.serviceId}")
    private String serviceId;

    @Value("${naver-sms.senderPhone}")
    private String phone;

    @Override
    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }

    @Override
    @Transactional(readOnly = false)
    public SmsResponseDto sendSms(String targetNumber) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        List<MessageDto> messages = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        //[Fithub] 인증번호 [ ㅡ ]를 입력해주세요
        sb.append("[Fithub] 인증번호 [");

        Integer randomNum = RandomNumberGenerator();
        String authNum = String.valueOf(randomNum);

        sb.append(authNum);

        sb.append("]를 입력해주세요");

        String content = String.valueOf(sb);
        messages.add(MessageDto.builder()
                .to(targetNumber)
                .content(content)
                .build());

        SmsRequestDto request = SmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content(content)
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);

        PhoneAuth phoneAuth = PhoneAuthConverter.toPhoneAuth(SmsDto.PhoneAuthDto
                .builder()
                .phoneNum(targetNumber)
                .authNum(randomNum)
                .sendTime(LocalDateTime.now())
                .build()
        );

        phoneAuthRepository.deleteByPhoneNum(targetNumber);
        phoneAuthRepository.save(phoneAuth);

        return response;
    }

    @Override
    @Transactional(readOnly = false)
    public SmsResponseDto.AuthNumResultDto authNumber(Integer authNum, String phoneNum) {
        PhoneAuth phoneAuth = phoneAuthRepository.findByPhoneNum(phoneNum).orElseThrow(() -> new PhoneAuthException(ErrorCode.PHONE_AUTH_NOT_FOUND));

        if (!phoneAuth.getAuthNum().equals(authNum))
                throw new PhoneAuthException(ErrorCode.PHONE_AUTH_ERROR);
        else{
            LocalDateTime nowTime = LocalDateTime.now();

            long timeCheck = ChronoUnit.MINUTES.between(phoneAuth.getSendDate(), nowTime);
            if (timeCheck >= 5)
                throw new PhoneAuthException(ErrorCode.PHONE_AUTH_TIMEOUT);
        }

        System.out.println(phoneAuth.getPhoneNum());
        phoneAuthRepository.deleteByPhoneNum(phoneAuth.getPhoneNum());

        return SmsResponseDto.AuthNumResultDto.builder()
                .responseCode(ResponseCode.SUCCESS)
                .build();
    }

    public Integer RandomNumberGenerator(){
        int min = 100000;
        int max = 999999;
        int random = ThreadLocalRandom.current().nextInt(min, max + 1);
        return random;
    }
}
