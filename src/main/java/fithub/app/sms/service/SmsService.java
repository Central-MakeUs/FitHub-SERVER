package fithub.app.sms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fithub.app.sms.dto.MessageDto;
import fithub.app.sms.dto.SmsResponseDto;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException;

    public Integer sendSms(String targetNumber) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;

    public SmsResponseDto.AuthNumResultDto authNumber(Integer authNum, String phoneNum);
}
