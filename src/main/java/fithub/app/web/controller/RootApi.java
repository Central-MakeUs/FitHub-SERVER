package fithub.app.web.controller;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.converter.RootConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.User;
import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.UserException;
import fithub.app.repository.UserRepository;
import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.RootApiResponseDto;
import fithub.app.web.dto.common.BaseDto;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RootApi {

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    @GetMapping("/health")
    public String health() {
        return "I'm healthy";
    }

    @GetMapping("/")
    public ResponseEntity<BaseDto.BaseResponseDto> AutoLogin(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, HttpServletRequest request){


        ResponseCode result = null;

        if(authorizationHeader == null)
            result = ResponseCode.AUTO_LOGIN_NEW_FACE;
        else{
            String token = authorizationHeader.substring(7);
            System.out.println(token);
            Long userId = tokenProvider.validateAndReturnId(token);
            User user = userRepository.findById(userId).orElseThrow(()-> new UserException(ErrorCode.MEMBER_NOT_FOUND));

            if (user.getAge() != null && user.getGender() != null)
                result = ResponseCode.AUTO_LOGIN_SUCCESS;
            else
                result = ResponseCode.AUTO_LOGIN_INFO_NULL;
        }
        return ResponseEntity.ok(BaseConverter.toBaseDto(result, null));
    }
}
