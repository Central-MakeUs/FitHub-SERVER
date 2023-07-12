package fithub.app.web.controller;

import fithub.app.auth.provider.TokenProvider;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.User;
import fithub.app.exception.common.ErrorCode;
import fithub.app.exception.handler.UserException;
import fithub.app.repository.UserRepository;
import fithub.app.utils.ResponseCode;
import fithub.app.web.dto.common.BaseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class RootApi {

    Logger logger = LoggerFactory.getLogger(RootApi.class);

    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    @GetMapping("/health")
    public String health() {
        return "I'm healthy";
    }

    @Operation(summary = "자동 로그인 API", description = "자동 로그인 API 입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK : 정상응답, 로그인 된 사용자 code : 2001, 회원가입 후 정보 입력이 필요한 사용자 code : 2002, 처음 온 사용자 code : 2003", content = @Content(schema = @Schema(implementation = BaseDto.BaseResponseDto.class))),

    })
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
