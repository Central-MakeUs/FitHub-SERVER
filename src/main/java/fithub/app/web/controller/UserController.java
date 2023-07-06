package fithub.app.web.controller;

import fithub.app.converter.UserConverter;
import fithub.app.domain.User;
import fithub.app.service.UserService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.UserRequestDto;
import fithub.app.web.dto.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/users/login/social/kakao")
    public ResponseEntity<UserResponseDto.OauthDto> kakaoOauth(@ModelAttribute UserRequestDto.socialDto request){

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(request.getSocialId());

        return ResponseEntity.ok(UserConverter.toOauthDto(result));
    }

    @GetMapping("/users/hi")
    public String testAPI(){
        return "ssss";
    }

}

