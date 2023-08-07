package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.converter.RootConverter;
import fithub.app.domain.BestRecorder;
import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.domain.User;
import fithub.app.service.HomeService;
import fithub.app.service.RootService;
import fithub.app.service.UserService;
import fithub.app.web.dto.requestDto.RootRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "홈 화면 및 기타 API", description = "홈 화면과 기타 API!")
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

    private final UserService userService;

    private final HomeService homeService;

    private final RootService rootService;

    private final TokenProvider tokenProvider;

    @GetMapping("/health")
    public String health() {
        return "I'm healthy";
    }

    @Operation(summary = "자동 로그인 API ✔️🔑", description = "자동 로그인 API 입니다. 이제 FCM 토큰도 주셔야 합니다!")
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2008", description = "OK : 정상응답, 바로 홈 화면으로 이동해도 될 경우" ,content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2009", description = "OK : 정상응답, 로그인 화면으로 이동해야 할 경우",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "UNAUTHORIZED : 토큰의 사용자가 존재하지 않는 사용자일 경우",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))

    })
    @GetMapping("/")
    public ResponseDto<RootApiResponseDto.AutoLoginResponseDto> AutoLogin(String authorizationHeader, HttpServletRequest request, @RequestBody @Valid RootRequestDto.AutoLoginDto fcm){


        Code result = null;
        Long userId = null;
        String accessToken = null;
        if(authorizationHeader == null)
            result = Code.AUTO_LOGIN_NOT_MAIN;
        else{
            String token = authorizationHeader.substring(7);
            userId = tokenProvider.validateAndReturnId(token);
            User user = userService.findUser(userId);

            logger.info("현재 스플래시 화면의 유저 : {}", user.getId());
            logger.info("현재 스플래시 화면의 유저 : {}", user.getNickname());
            logger.info("현재 스플래시 화면의 유저 : {}", user.getName());
            logger.info("현재 스플래시 화면의 유저 : {}", user.getAge());
            logger.info("현재 스플래시 화면의 유저 : {}", user.getGender());
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

    @Operation(summary = "홈 화면 조회 API ✔️🔑", description = "홈 화면 조회 API 입니다.")
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))

    })
    @GetMapping("/home")
    public ResponseDto<RootApiResponseDto.HomeProfileDto> getHomeProfile(@AuthUser User user){
        List<BestRecorder> bestRecorderList = homeService.findBestRecorderList();
        return ResponseDto.of(RootConverter.toHomeProfileDto(user,bestRecorderList));
    }

    @Operation(summary = "북마크 게시글 목록 조회 API ✔️🔑", description = "홈 화면 조회 API 입니다. pageIndex로 paging, 신고한 사용자는 숨김")
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "pageIndex", description = "페이징을 위한 페이지 번호, query String")
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 없는 카테고리 입니다..", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/home/book-mark/{categoryId}")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> showSavedArticle(@PathVariable(name = "categoryId") Integer categoryId, @RequestParam(name = "pageIndex") Integer pageIndex, @AuthUser User user){
        return ResponseDto.of(ArticleConverter.toArticleDtoList(userService.findSavedArticle(categoryId,pageIndex, user),user));
    }
    @Operation(summary = "레벨 설명 조회 API ✔️🔑", description = "핏허브 레벨 설명 조회 API 입니다. 내 메인 운동 레벨정보와 핏허브 레벨 정보가 담깁니다.")
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/home/level-info")
    public ResponseDto<RootApiResponseDto.LevelInfoDto> showLevelInfo(@AuthUser User user){
        List<Grade> gradeList = rootService.findAllGrade();
        LevelInfo levelInfo = rootService.findLevelInfo();
        return ResponseDto.of(RootConverter.toLevelInfoDto(user,gradeList,levelInfo));
    }
}
