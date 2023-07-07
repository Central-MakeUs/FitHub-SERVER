package fithub.app.web.controller;

import fithub.app.converter.UserConverter;
import fithub.app.domain.User;
import fithub.app.service.AppleService;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.UserRequestDto;
import fithub.app.web.dto.UserResponseDto;
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

    @PostMapping("/users/login/social/kakao")
    public ResponseEntity<UserResponseDto.OauthDto> kakaoOauth(@RequestBody UserRequestDto.socialDto request){

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(request.getSocialId());

        return ResponseEntity.ok(UserConverter.toOauthDto(result));
    }

    @PostMapping("/users/login/social/apple")
    public String appleOauth(@ModelAttribute UserRequestDto.AppleSocialDto request) throws IOException {
        String identityToken = request.getIdentityToken();
        String socialId;

        socialId = appleService.userIdFromApple(identityToken);

        return "temp";
    }
}

