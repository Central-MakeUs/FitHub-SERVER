package fithub.app.web.controller;

import fithub.app.converter.UserConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.User;
import fithub.app.service.AppleService;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.UserRequestDto;
import fithub.app.web.dto.UserResponseDto;
import fithub.app.web.dto.common.BaseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AppleService appleService;

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
        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(socialId);

        ResponseCode responseCode;

        if (result.getIsLogin())
            responseCode = ResponseCode.KAKAO_OAUTH_LOGIN;
        else
            responseCode = ResponseCode.KAKAO_OAUTH_JOIN;

        return ResponseEntity.ok(BaseConverter.toBaseDto(responseCode, UserConverter.toOauthDto(result)));
    }
}

