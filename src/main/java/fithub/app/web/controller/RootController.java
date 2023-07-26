package fithub.app.web.controller;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;
import fithub.app.converter.RootConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.User;
import fithub.app.base.exception.handler.UserException;
import fithub.app.domain.enums.SocialType;
import fithub.app.repository.UserRepository;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "홈 화면 및 기타 API", description = "홈 화면과 기타 API!")
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    @GetMapping("/health")
    public String health() {
        return "I'm healthy";
    }

    @Operation(summary = "자동 로그인 API", description = "자동 로그인 API 입니다. 🔑 스웨거 테스트 시 평소 하던 대로 토큰 넣어서 테스트 해주세요")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2008", description = "OK : 정상응답, 바로 홈 화면으로 이동해도 될 경우" ,content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2009", description = "OK : 정상응답, 로그인 화면으로 이동해야 할 경우",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "UNAUTHORIZED : 토큰의 사용자가 존재하지 않는 사용자일 경우",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))

    })
    @GetMapping("/")
    public ResponseDto<RootApiResponseDto.AutoLoginResponseDto> AutoLogin(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, HttpServletRequest request){


        Code result = null;
        Long userId = null;
        String accessToken = null;
        if(authorizationHeader == null)
            result = Code.AUTO_LOGIN_NOT_MAIN;
        else{
            String token = authorizationHeader.substring(7);
            System.out.println(token);
            userId = tokenProvider.validateAndReturnId(token);
            User user = userRepository.findById(userId).orElseThrow(()-> new UserException(Code.MEMBER_NOT_FOUND));

            if (user.getAge() == null || user.getGender() == null)
                result = Code.AUTO_LOGIN_NOT_MAIN;
            else {
                result = Code.AUTO_LOGIN_MAIN;
                if (user.getIsSocial())
                    accessToken = tokenProvider.createAccessToken(userId, user.getSocialType().toString(),Arrays.asList(new SimpleGrantedAuthority("USER")));
                else
                    accessToken = tokenProvider.createAccessToken(userId,user.getPhoneNum(),Arrays.asList(new SimpleGrantedAuthority("USER")));
            }
        }
        return ResponseDto.of(result,RootConverter.toAutoLoginResponseDto(userId,accessToken));
    }
}
