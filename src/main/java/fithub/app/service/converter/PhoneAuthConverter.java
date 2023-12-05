package fithub.app.service.converter;

import fithub.app.domain.PhoneAuth;
import fithub.app.repository.PhoneAuthRepository;
import fithub.app.sms.dto.SmsDto;
import fithub.app.sms.dto.SmsResponseDto;
import fithub.app.utils.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class PhoneAuthConverter {

    private final PhoneAuthRepository phoneAuthRepository;
    private static PhoneAuthRepository staticPhoneAuthRepository;

    @PostConstruct
    public void init() {
        this.staticPhoneAuthRepository = this.phoneAuthRepository;
    }

    public static PhoneAuth toPhoneAuth(SmsDto.PhoneAuthDto phoneAuthDto){
        return PhoneAuth.builder()
                .phoneNum(phoneAuthDto.getPhoneNum())
                .authNum(phoneAuthDto.getAuthNum())
                .sendDate(phoneAuthDto.getSendTime())
                .build();
    }

    public static SmsResponseDto.AuthNumResultDto toAuthNumResultDto(ResponseCode responseCode){
        return SmsResponseDto.AuthNumResultDto.builder()
                .responseCode(responseCode)
                .build();
    }
}
