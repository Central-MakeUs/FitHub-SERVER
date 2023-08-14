package fithub.app.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;

import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.converter.UserConverter;
import fithub.app.converter.common.BaseConverter;
import fithub.app.domain.*;
import fithub.app.domain.mapping.UserReport;
import fithub.app.service.AppleService;
import fithub.app.service.UserService;
import fithub.app.sms.dto.SmsResponseDto;
import fithub.app.sms.service.SmsService;
import fithub.app.utils.OAuthResult;
import fithub.app.web.dto.common.BaseDto;
import fithub.app.web.dto.requestDto.UserRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.swing.plaf.PanelUI;
import javax.validation.Valid;
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
@Tag(name = "유저 관련 API", description = "로그인, 회원가입, 마이 페이지에서 필요한 API모음")
public class UserRestController {

    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    private final AppleService appleService;

    private final SmsService smsService;

    @Operation(summary = "카카오 소셜 로그인 ✔️", description = "카카오 소셜 로그인 API 입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2004", description = "OK : 정상응답, 로그인 프로세스"),
            @ApiResponse(responseCode = "2005", description = "OK : 정상응답, 회원가입 프로세스"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/users/login/social/kakao")
    public ResponseDto<OAuthResult.OAuthResultDto> kakaoOauth(@RequestBody UserRequestDto.socialDto request){

        logger.info("/login/social/kakao 넘겨 받은  body : {}",request.toString());

        OAuthResult.OAuthResultDto result = userService.kakaoOAuth(request.getSocialId());

        Code responseCode;

        if (result.getIsLogin())
            responseCode = Code.KAKAO_OAUTH_LOGIN;
        else
            responseCode = Code.KAKAO_OAUTH_JOIN;

        return ResponseDto.of(responseCode, result);
    }

    @Operation(summary = "애플 소셜 로그인 ✔️", description = "애플 소셜 로그인 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2004", description = "OK : 정상응답, 로그인 프로세스"),
            @ApiResponse(responseCode = "2005", description = "OK : 정상응답, 회원가입 프로세스"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))

    })
    @PostMapping("/users/login/social/apple")
    public ResponseDto<OAuthResult.OAuthResultDto> appleOauth(@RequestBody UserRequestDto.AppleSocialDto request) throws IOException {


        logger.info("/login/social/apple 넘겨 받은 body : {}", request.getIdentityToken());

        String identityToken = request.getIdentityToken();
        String socialId;

        socialId = appleService.userIdFromApple(identityToken);

        logger.info("userId from apple service : {}", socialId);

        OAuthResult.OAuthResultDto result = userService.appleOAuth(socialId);

        Code responseCode;

        if (result.getIsLogin())
            responseCode = Code.APPLE_OAUTH_LOGIN;
        else
            responseCode = Code.APPLE_OAUTH_JOIN;

        logger.info("애플 소셜로그인의 결과 : {}", responseCode);
        return ResponseDto.of(responseCode, result);
    }


    @Operation(summary = "닉네임 중복 검사 ✔️", description = "닉네임 중복검사 API 입니다.")
    @GetMapping("/users/exist-nickname")
    @ApiResponses({
            @ApiResponse(responseCode = "2010", description = "OK : 정상응답, 닉네임이 이미 사용중인 경우!"),
            @ApiResponse(responseCode = "2011", description = "OK : 정상응답, 닉네임 사용이 가능함!"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    public ResponseDto<String> getExistNickname(
            @ApiParam (value = "검사를 원하는 닉네임", readOnly = true) @RequestParam String nickname
    ){

        logger.info("passed nickname from front : {}", nickname);

        Code responseCode= null;

        Optional<User> user = userService.checkExistNickname(nickname);

        if (user.isPresent())
            responseCode = Code.NICKNAME_EXIST;
        else
            responseCode = Code.NICKNAME_OK;

        return ResponseDto.of(responseCode, nickname);
    }

    @Operation(summary = "운동 카테고리 리스트 API ✔️", description = "회원가입 시 선호하는 카테고리를 선택할 때 목록을 받아오기 위해 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답")
    })
    @GetMapping("/users/exercise-category")
    public ResponseDto<List<ExerciseCategory>> getExerciseCategoryList(){
        List<ExerciseCategory> exerciseList = userService.getExerciseList();

        logger.info("운동 카테고리 리스트 : {}", exerciseList);

        return ResponseDto.of(exerciseList);
    }

    @Operation(summary = "핸드폰 번호를 이용한 회원가입 완료 API ✔️", description = "핸드폰 번호를 이용한 회원가입 시 사용됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 가입 한 사용자의 DB 상 id, nickname이 담긴 result 반환"),
            @ApiResponse(responseCode = "4017", description = "BAD_REQUEST : 운동 카테고리가 잘못 됨"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(value = "/users/sign-up",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<UserResponseDto.JoinUserDto> signUpByPhoneNum(@ModelAttribute @Valid UserRequestDto.UserInfo request) throws IOException
    {

        logger.info("넘겨 받은 사용자의 정보 : {}", request.toString());

        User user = userService.signUpPhoneNum(request);

        System.out.println(user.getPhoneNum());
        UserResponseDto.JoinUserDto createdUser = UserConverter.toJoinUserDto(user);
        return ResponseDto.of(createdUser);
    }

    @Operation(summary = "소셜로그인 회원정보 최종입력 API ✔️", description = "소셜로그인으로 가입 시 회원정보를 최종으로 기입하기 위해 사용됩니다, 토요일에 작업할게용")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 가입 한 사용자의 DB 상 id, nickname이 담긴 result 반환"),
            @ApiResponse(responseCode = "4017", description = "BAD_REQUEST : 운동 카테고리가 잘못 됨"),
            @ApiResponse(responseCode = "4013", description = "UNAUTHORIZED : 토큰에 해당하는 사용자가 존재하지 않음"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PatchMapping(value = "/users/sign-up/oauth",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<UserResponseDto.SocialInfoDto> signUpByOAuth(@ModelAttribute UserRequestDto.UserOAuthInfo request, @AuthUser User user) throws IOException
    {
        logger.info("넘겨받은 사용자의 정보 : {}", request.toString());
        User updatedUser = userService.socialInfoComplete(request, user);
        return ResponseDto.of(UserConverter.toSocialInfoDto(updatedUser));
    }

    @Operation(summary = "핸드폰으로 전송된 인증 번호 검증 API ✔️", description = "body에 사용자의 핸드폰 번호와 인증 번호 2개를 주세요!")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 인증 성공"),
            @ApiResponse(responseCode = "4014", description = "UNAUTHORIZED 인증 번호가 틀린 경우", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
                @ApiResponse(responseCode = "4015", description = "UNAUTHORIZED 유효시간이 지난 경우", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4016", description = "BAD REQUEST 인증번호를 받은 번호가 없는 경우", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))

    })
    @PostMapping("/users/sms/auth")
    public ResponseDto<SmsResponseDto.AuthNumResultDto> authPhoneNum(@RequestBody UserRequestDto.PhoneNumAuthDto request){
        SmsResponseDto.AuthNumResultDto authNumResultDto = smsService.authNumber(request.getAuthNum(), request.getPhoneNum());
        return ResponseDto.of(authNumResultDto);
    }

    @Operation(summary = "핸드폰 인증번호 생성 API ✔️", description = "사용자의 번호를 body에 넘겨 줘 인증번호가 문자로 송신되게 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/users/sms")
    public ResponseDto<Integer> sendSms(@RequestBody UserRequestDto.SmsRequestDto request) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        Integer data = smsService.sendSms(request.getTargetPhoneNum());
        return ResponseDto.of(data);
    }

    @Operation(summary = "비밀번호 찾기 단계 핸드폰 인증 API ✔️",description = "비밀번호를 찾기 전 핸드폰 인증을 하는 단계입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4019", description = "BAD_REQUEST : 가입한 회원이 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/users/password")
    public ResponseDto<Integer> findPass(@RequestBody UserRequestDto.FindPassDto request) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {

        logger.info("/users/password 클라이언트가 준 정보 : {}", request.toString());

        User user = userService.findByPhoneNum(request.getTargetPhoneNum());
        Integer data = smsService.sendSms(user.getPhoneNum());
        return ResponseDto.of(data);
    }

    @Operation(summary = "비밀번호 변경 API ✔️",description = "유저 식별을 위해 앞서 입력한 핸드폰 번호와 새 비밀번호를 주세요.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4019", description = "BAD_REQUEST : 가입한 회원이 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PatchMapping("/users/password")
    public ResponseDto<UserResponseDto.PassChangeDto> changePass(@RequestBody UserRequestDto.ChangePassDto request){
        User user = userService.updatePassword(request.getTargetPhoneNum(), request.getNewPassword());
        return ResponseDto.of(UserConverter.toPassChangeDto(request.getNewPassword()));
    }

    @Operation(summary = "로그인 API ✔️", description = "로그인 API입니다. 비밀번호를 주세요")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4019", description = "BAD_REQUEST : 가입한 회원이 없음",content =@Content(schema =  @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/users/sign-in")
    public ResponseDto<UserResponseDto.LoginResultDto> login(@RequestBody UserRequestDto.LoginDto request){
        User user = userService.findByPhoneNum(request.getTargetPhoneNum());
        String jwt = userService.login(user,request.getPassword());
        logger.info("로그인 토큰 : {}", jwt);

        return ResponseDto.of(UserConverter.toLoginDto(jwt, user));
    }

    @Operation(summary = "내가 적은 게시글 목록 조회 API ✔️🔑- 최신순  ", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/articles/{categoryId}")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> myArticles(@RequestParam(name = "pageIndex", required = false) Integer pageIndex,@PathVariable(name = "categoryId") Integer categoryId ,@AuthUser User user){
        Page<Article> articles = categoryId.equals(0) ? userService.getMyArticlesNoCategory(pageIndex,user) : userService.getMyArticles(pageIndex, user,categoryId);
        return ResponseDto.of(ArticleConverter.toArticleDtoList(articles, user,categoryId.equals(0)));
    }

    @Operation(summary = "내가 적은 운동 인증 목록 조회 API ✔️🔑- 최신순 ", description = "categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠"),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/records/{categoryId}")
    public ResponseDto<RecordResponseDto.recordDtoList> myRecords(@RequestParam(name = "pageIndex", required = false) Integer pageIndex, @PathVariable(name = "categoryId")Integer categoryId, @AuthUser User user){
        Page<Record> records = categoryId.equals(0) ? userService.getMyRecordsNoCategory(pageIndex, user) : userService.getMyRecords(pageIndex,user,categoryId);
        return ResponseDto.of(RecordConverter.toRecordDtoList(records, user));
    }

    @Operation(summary = "이미 가입된 번호인지 체크하는 API ✔️", description = "이미 가입된 번호인지 체크하는 API 입니다. 타입이 0이면 이미 있는 번호인지? 타입이 1이면 가입된 번호인지? 판단하며 후자는 비밀번호 재 설정 시 사용이 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답",content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4018", description = "BAD_REQUEST : 이미 가입된 번호. 타입이 0일 때만 이 응답이 감", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4019", description = "BAD_REQUEST : 가입 된 적 없는 번호, 타입이 1일 때만 이 응답이 감", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/users/exist-phone/{type}")
    public ResponseDto<BaseDto.BaseResponseDto> checkExistPhone(@RequestBody UserRequestDto.findExistPhoneDto request, @PathVariable(name = "type") Integer type){
        if (type == 0)
            userService.findByPhoneNumJoin(request.getTargetPhoneNum());
        else
            userService.findByPhoneNumPassChange(request.getTargetPhoneNum());
        return ResponseDto.of(Code.OK, null);
    }

    @Operation(summary = "마이 페이지 조회 API ✔️ 🔑", description = "마이 페이지를 조회하는 API 입니다. 운동 종목 중 첫 번째는 메인 운동입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/my-page")
    public ResponseDto<UserResponseDto.MyPageDto> getMyPage(@AuthUser User user){
        List<UserExercise> myExercises = userService.getMyExercises(user);
        return ResponseDto.of(UserConverter.toMyPageDto(user, myExercises));
    }

    @Operation(summary = "마이 페이지 - 내 프로필 변경 ✔️ 🔑", description = "마이 페이지에서 프로필 변경하는 API 입니다")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping (value = "/users/my-page/profile",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<UserResponseDto.ChangeMyProfileDto> changeMyProfile(@AuthUser User user, @ModelAttribute UserRequestDto.ChangeMyProfileDto request) throws IOException
    {
        String profile = userService.changeMyProfile(user, request);
        return ResponseDto.of(UserConverter.toChangeMyProfileDto(profile));
    }

    @Operation(summary = "내 메인운동 바꾸기 API ✔️ 🔑", description = "메인 운동을 바꾸는 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping("/users/my-page/main-exercise/{categoryId}")
    public ResponseDto<UserResponseDto.MainExerciseChangeDto> changeMainExercise(@PathVariable(name = "categoryId") Integer categoryId, @AuthUser User user){
        UserExercise userExercise = userService.patchMainExercise(user, categoryId);
        return ResponseDto.of(UserConverter.toMainExerciseChangeDto(userExercise));
    }

    @Operation(summary = "사용자 신고하기 API ✔️ 🔑", description = "사용자 신고하기 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST : 없는 유저입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4062", description = "BAD_REQUEST : 이미 신고했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4063", description = "BADE_REQUEST : 스스로 신고는 안됩니다.",content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PostMapping("/users/{userId}/report")
    public ResponseDto<UserResponseDto.ReportUserDto> reportUser(@PathVariable(name = "userId") Long userId,@AuthUser User user){
        UserReport userReport = userService.reportUser(userId, user);
        return ResponseDto.of(UserConverter.toReportUserDto(userId, userReport));
    }

    @Operation(summary = "사용자 프로필 조회하기 API ✔️ 🔑", description = "사용자 프로필 조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST : 없는 유저입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4064", description = "FORBIDDEN : 조회가 할 수 없는 사용자 입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "userId", description = "사용자의 아이디"),
    })
    @GetMapping("/users/{userId}")
    public ResponseDto<UserResponseDto.OtherUserProfileDto> showProfile(@PathVariable(name = "userId") Long userId, @AuthUser User user){
        User findUser = userService.findUserNotBlocked(userId,user);
        return ResponseDto.of(UserConverter.toOtherUserProfileDto(findUser));
    }

    @Operation(summary = "조회 한 사용자의 게시글 목록 조회 API ✔️ 🔑", description = "조회 한 사용자의 게시글 목록 조회 API 입니다. 카테고리가 0이면 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST : 없는 유저입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 없는 카테고리입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "userId", description = "사용자의 아이디"),
            @Parameter(name = "pageIndex", description = "페이징을 위한 페이지 번호, query String")
    })
    @GetMapping("/users/{userId}/articles/{categoryId}")
    public ResponseDto<ArticleResponseDto.ArticleDtoList> showArticleList(@PathVariable(name = "userId")Long userId, @PathVariable(name = "categoryId") Integer categoryId,@RequestParam(name = "pageIndex") Integer pageIndex, @AuthUser User user){
        return ResponseDto.of(ArticleConverter.toArticleDtoList(userService.findUserArticle(userId,categoryId,pageIndex),user,categoryId.equals(0)));
    }

    @Operation(summary = "조회 한 사용자의 운동인증 목록 조회 API ✔️ 🔑", description = "조회 한 사용자의 운동인증 목록 조회 API 입니다. 카테고리가 0이면 전체 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST : 없는 유저입니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 없는 카테고리 입니다..", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "userId", description = "사용자의 아이디"),
            @Parameter(name = "pageIndex", description = "페이징을 위한 페이지 번호, query String")
    })
    @GetMapping("/users/{userId}/records/{categoryId}")
    public ResponseDto<RecordResponseDto.recordDtoList> showRecordList(@PathVariable(name = "userId")Long userId, @PathVariable(name = "categoryId") Integer categoryId,@RequestParam(name = "pageIndex") Integer pageIndex, @AuthUser User user){
        return ResponseDto.of(RecordConverter.toRecordDtoList(userService.findUserRecord(userId,categoryId,pageIndex),user));
    }

    @Operation(summary = "나의 현재 메인 운동 종목 조회 API ✔️ 🔑", description = "나의 현재 메인 운동 종목 조회 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/users/main-exercise")
    public ResponseDto<UserResponseDto.CurrentMainExerciseDto> showCurrentMain(@AuthUser User user){
        User findUser = userService.findUser(user.getId());
        return ResponseDto.of(UserConverter.toCurrentMainExerciseDto(findUser));
    }

    @Operation(summary = "나의 프로필 이미지 기본으로 변경 API ✔️ 🔑", description = "나의 프로필 이미지 기본으로 변경 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping("/users/my-page/profile/default")
    public ResponseDto<UserResponseDto.ChangeDefaultImageDto> changeDefault(@AuthUser User user){
        userService.changeMyProfileDefault(user);
        return ResponseDto.of(UserConverter.toChangeDefaultImageDto());
    }

    @Operation(summary = "안 읽은 알림 있는지 확인 하는 API ✔️ 🔑", description = "안 읽은 알림 있는지 확인 하는 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/user/my-alarm")
    public ResponseDto<UserResponseDto.AlarmRemainDto> checkRemainAlarm(@AuthUser User user){
        Long aLong = userService.checkRemainAlarm(user);
        return ResponseDto.of(UserConverter.toAlarmRemainDto(aLong > 0));
    }

    @Operation(summary = "내 개인정보 확인 API ✔️ 🔑", description = "내 개인정보 확인 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/user/my-page/personal-data")
    public ResponseDto<UserResponseDto.ShowPersonalDataDto> showPersonalData(@AuthUser User user){
        return ResponseDto.of(UserConverter.toShowPersonalDataDto(userService.findUser(user.getId())));
    }

    @Operation(summary = "이미 가입은 완료된 상태에서 fcm token 추가 API ✔️ 🔑", description = "앱을 다시 설치 할 경우 fcm 토큰 추가를 위한 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PostMapping("/users/fcm-token")
    public ResponseDto<UserResponseDto.FcmTokenUpdateDto> AddFcmToken(@RequestBody UserRequestDto.FcmTokenDto request, @AuthUser User user){
        userService.addFcmToken(user, request.getFcmToken());
        return ResponseDto.of(UserConverter.toFcmTokenUpdateDto());
    }

    @Operation(summary = "비밀번호 확인 API ✔️ 🔑", description = "비밀번호 변경 전, 비밀번호 확인을 위한 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "2022", description = "OK : 비밀번호가 일치합니다."),
            @ApiResponse(responseCode = "2023", description = "OK : 비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PostMapping("/users/check-pass")
    public ResponseDto checkPass(@RequestBody UserRequestDto.CheckPassDto request, @AuthUser User user){
        Boolean checkPass = userService.checkPass(user, request);
        Code result = checkPass ? Code.PASSWORD_CORRECT : Code.PASSWORD_INCORRECT;
        return ResponseDto.of(result,null);
    }
}