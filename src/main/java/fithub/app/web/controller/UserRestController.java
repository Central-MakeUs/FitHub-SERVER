package fithub.app.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fithub.app.converter.ExerciseCategoryConverter;

import fithub.app.converter.UserConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.service.AppleService;
import fithub.app.service.UserService;
import fithub.app.sms.dto.SmsResponseDto;
import fithub.app.sms.service.SmsService;
import fithub.app.utils.OAuthResult;
import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.requestDto.ExerciseCategoryRequestDto;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.common.BaseDto;
import fithub.app.web.dto.responseDto.ExerciseCategoryResponseDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@Validated
@RequiredArgsConstructor
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    private final AppleService appleService;

    private final SmsService smsService;

    @Operation(summary = "카카오 소셜 로그인", description = "카카오 소셜 로그인 API 입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK : 정상응답, 로그인 프로세스 code : 2004, 회원가입 프로세스 code : 2005", content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
    })
    @PostMapping("/users/login/social/kakao")
    public ResponseEntity<BaseDto.BaseResponseDto> kakaoOauth(@RequestBody UserRequestDto.socialDto request){

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(request.getSocialId());

        ResponseCode responseCode;

        if (result.getIsLogin())
            responseCode = ResponseCode.KAKAO_OAUTH_LOGIN;
        else
            responseCode = ResponseCode.KAKAO_OAUTH_JOIN;

        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, UserConverter.toOauthDto(result)));
    }

    @Operation(summary = "애플 소셜 로그인", description = "애플 소셜 로그인 API 입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK : 정상응답, 로그인 프로세스 code : 2004, 회원가입 프로세스 code : 2005", content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
    })
    @PostMapping("/users/login/social/apple")
    public ResponseEntity<BaseDto.BaseResponseDto> appleOauth(@RequestBody UserRequestDto.AppleSocialDto request) throws IOException {
        String identityToken = request.getIdentityToken();
        String socialId;

        socialId = appleService.userIdFromApple(identityToken);

        logger.info("userId from apple service : {}", socialId);

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(socialId);

        ResponseCode responseCode;

        if (result.getIsLogin())
            responseCode = ResponseCode.KAKAO_OAUTH_LOGIN;
        else
            responseCode = ResponseCode.KAKAO_OAUTH_JOIN;

        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, UserConverter.toOauthDto(result)));
    }


    @GetMapping("/users/exist-nickname")
    public ResponseEntity<BaseDto.BaseResponseDto> getExistNickname(@RequestParam String nickname){
        return null;
    }

    @GetMapping("/users/exercise-category")
    public ResponseEntity<BaseDto.BaseResponseDto> getExerciseCategoryList(){
        return null;
    }

    @PostMapping("/users/sign-up")

    public ResponseEntity<BaseDto.BaseResponseDto> signUpByPhoneNum(@RequestBody UserRequestDto.UserInfo request){
        return null;
    }

    @PostMapping("/users/sign-up/oauth")
    public ResponseEntity<BaseDto.BaseResponseDto> signUpByOAuth(@RequestBody UserRequestDto.UserOAuthInfo request){
        return null;
    }

    @PostMapping("/users/sms")
    public ResponseEntity<BaseDto.BaseResponseDto> sendSms(@RequestBody UserRequestDto.SmsRequestDto request) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        SmsResponseDto data = smsService.sendSms(request.getTargetPhoneNum());
        return ResponseEntity.ok(BaseConverter.toBaseDto(ResponseCode.SUCCESS, null));
    }

    @PostMapping("/users/sms/auth")
    public ResponseEntity<BaseDto.BaseResponseDto> authPhoneNum(@RequestBody UserRequestDto.PhoneNumAuthDto request){
        SmsResponseDto.AuthNumResultDto authNumResultDto = smsService.authNumber(request.getAuthNum(), request.getPhoneNum());
        return ResponseEntity.ok(BaseConverter.toBaseDto(authNumResultDto.getResponseCode(), null));
    }
}

