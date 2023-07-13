package fithub.app.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fithub.app.converter.ExerciseCategoryConverter;

import fithub.app.converter.UserConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.User;
import fithub.app.exception.advice.ResponseFormat;
import fithub.app.exception.common.ErrorCode;
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
import io.swagger.annotations.ApiParam;
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
import java.util.List;
import java.util.Optional;

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

        logger.info("/login/social/kakao 넘겨 받은  body : {}",request.toString());

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(request.getSocialId());

        ResponseCode responseCode;

        if (result.getIsLogin())
            responseCode = ResponseCode.KAKAO_OAUTH_LOGIN;
        else
            responseCode = ResponseCode.KAKAO_OAUTH_JOIN;

        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, UserConverter.toOauthDto(result)));
    }

    @Operation(summary = "애플 소셜 로그인", description = "애플 소셜 로그인 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2004", description = "OK : 정상응답, 로그인 프로세스", content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
            @ApiResponse(responseCode = "2005", description = "OK : 정상응답, 회원가입 프로세스", content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
            @ApiResponse(responseCode = "4011", description = "UNAUTHORIZED : 애플 auth 서버로부터 사용할 공개키 목록을 받지 못 한 경우", content = @Content(schema = @Schema(implementation = ResponseFormat.class))),
            @ApiResponse(responseCode = "4012", description = "UNAUTHORIZED : Identity token 에러",content = @Content(schema = @Schema(implementation = ResponseFormat.class))),

    })
    @PostMapping("/users/login/social/apple")
    public ResponseEntity<BaseDto.BaseResponseDto> appleOauth(@RequestBody UserRequestDto.AppleSocialDto request) throws IOException {


        logger.info("/login/social/apple 넘겨 받은 body : {}", request.getIdentityToken());

        String identityToken = request.getIdentityToken();
        String socialId;

        socialId = appleService.userIdFromApple(identityToken);

        logger.info("userId from apple service : {}", socialId);

        OAuthResult.OAuthResultDto result = userService.appleOAuth(socialId);

        ResponseCode responseCode;

        if (result.getIsLogin())
            responseCode = ResponseCode.APPLE_OAUTH_LOGIN;
        else
            responseCode = ResponseCode.APPLE_OAUTH_JOIN;

        logger.info("애플 소셜로그인의 결과 : {}", responseCode);
        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, UserConverter.toOauthDto(result)));
    }


    @Operation(summary = "닉네임 중복 검사", description = "닉네임 중복검사 API 입니다.")
    @GetMapping("/users/exist-nickname")
    @ApiResponses({
            @ApiResponse(responseCode = "2010", description = "OK : 정상응답, 닉네임이 이미 사용중인 경우!",content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
            @ApiResponse(responseCode = "2011", description = "OK : 정상응답, 닉네임 사용이 가능함!",content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
    })
    public ResponseEntity<BaseDto.BaseResponseDto> getExistNickname(
            @ApiParam (value = "검사를 원하는 닉네임", readOnly = true) @RequestParam String nickname
    ){

        logger.info("passed nickname from front : {}", nickname);

        ResponseCode responseCode= null;

        Optional<User> user = userService.checkExistNickname(nickname);

        if (user.isPresent())
            responseCode = ResponseCode.NICKNAME_EXIST;
        else
            responseCode = ResponseCode.NICKNAME_OK;

        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, null));
    }

    @Operation(summary = "운동 카테고리 리스트 API", description = "회원가입 시 선호하는 카테고리를 선택할 때 목록을 받아오기 위해 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답")
    })
    @GetMapping("/users/exercise-category")
    public ResponseEntity<BaseDto.BaseResponseDto> getExerciseCategoryList(){
        List<ExerciseCategory> exerciseList = userService.getExerciseList();

        logger.info("운동 카테고리 리스트 : {}", exerciseList);

        return ResponseEntity.ok(BaseConverter.toBaseDto(ResponseCode.SUCCESS,ExerciseCategoryConverter.toCategoryFullDtoList(exerciseList)));
    }

    @PostMapping("/users/sign-up")
    @Operation(summary = "핸드폰 번호를 이용한 회원가입 완료 API", description = "핸드폰 번호를 이용한 회원가입 시 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 가입 한 사용자의 DB 상 id, nickname이 담긴 result 반환",content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
            @ApiResponse(responseCode = "4017", description = "BAD_REQUEST : 선호하는 카테고리가 없는 카테고리인 경우", content = @Content(schema = @Schema(implementation = ResponseFormat.class)))
    })
    public ResponseEntity<BaseDto.BaseResponseDto> signUpByPhoneNum(@RequestBody UserRequestDto.UserInfo request){

        logger.info("넘겨 받은 사용자의 정보 : {}", request.toString());

        User user = userService.signUpPhoneNum(request);

        UserResponseDto.JoinUserDto createdUser = UserConverter.toJoinUserDto(user);
        return ResponseEntity.ok(BaseConverter.toBaseDto(ResponseCode.SUCCESS, createdUser));
    }

    @Operation(summary = "소셜로그인 회원정보 최종입력 API", description = "소셜로그인으로 가입 시 회원정보를 최종으로 기입하기 위해 사용됩니다, 토요일에 작업할게용")
    @PatchMapping("/users/sign-up/oauth")
    public ResponseEntity<BaseDto.BaseResponseDto> signUpByOAuth(@RequestBody UserRequestDto.UserOAuthInfo request){
        return null;
    }

    @Operation(summary = "핸드폰으로 전송된 인증 번호 검증 API", description = "body에 사용자의 핸드폰 번호와 인증 번호 2개를 주세요!")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 인증 성공",content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),
            @ApiResponse(responseCode = "4014", description = "UNAUTHORIZED : 인증번호가 잘못 된 경우",content = @Content(schema = @Schema(implementation = ResponseFormat.class))),
            @ApiResponse(responseCode = "4015", description = "UNAUTHORIZED : 유효시간 5분이 지난 경우",content = @Content(schema = @Schema(implementation = ResponseFormat.class))),
            @ApiResponse(responseCode = "4016", description = "UNAUTHORIZED : 핸드폰 번호가 이상한 경우",content = @Content(schema = @Schema(implementation = ResponseFormat.class)))
    })
    @PostMapping("/users/sms/auth")
    public ResponseEntity<BaseDto.BaseResponseDto> authPhoneNum(@RequestBody UserRequestDto.PhoneNumAuthDto request){
        SmsResponseDto.AuthNumResultDto authNumResultDto = smsService.authNumber(request.getAuthNum(), request.getPhoneNum());
        return ResponseEntity.ok(BaseConverter.toBaseDto(authNumResultDto.getResponseCode(), null));
    }

    @Operation(summary = "핸드폰 인증번호 생성 API", description = "사용자의 번호를 body에 넘겨 줘 인증번호가 문자로 송신되게 합니다. 500 에러가 터지거나 성공하거나 모 아니면 도")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답")
    })
    @PostMapping("/users/sms")
    public ResponseEntity<BaseDto.BaseResponseDto> sendSms(@RequestBody UserRequestDto.SmsRequestDto request) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        SmsResponseDto data = smsService.sendSms(request.getTargetPhoneNum());
        return ResponseEntity.ok(BaseConverter.toBaseDto(ResponseCode.SUCCESS, null));
    }
}

