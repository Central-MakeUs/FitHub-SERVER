package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.auth.provider.TokenProvider;
import fithub.app.base.Code;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RootConverter;
import fithub.app.converter.UserConverter;
import fithub.app.domain.*;
import fithub.app.service.HomeService;
import fithub.app.service.KakaoLocalService;
import fithub.app.service.RootService;
import fithub.app.service.UserService;
import fithub.app.web.dto.requestDto.RootRequestDto;
import fithub.app.web.dto.responseDto.ArticleResponseDto;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialStruct;
import java.io.IOException;
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

    private final KakaoLocalService kakaoLocalService;

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
    public ResponseDto<RootApiResponseDto.AutoLoginResponseDto> AutoLogin(String authorizationHeader, HttpServletRequest request){


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

    @Operation(summary = "🚧 디비에 운동시설 저장하기, 서버 개발자가 사용함 🚧", description = "운동 시설 디비 저장 용, 쓰지 마세여")
    @Parameters({
            @Parameter(name = "keyword", description = "카카오 로컬 키워드"),
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping("/home/facilities/{categoryId}")
    public ResponseDto<RootApiResponseDto.SaveFacilitiesDto> saveFacilities(@RequestParam(name = "keyword") String keyword, @PathVariable(name = "categoryId") Integer categoryId){
        Integer saved = kakaoLocalService.saveFacilities(keyword, categoryId);
        return ResponseDto.of(RootConverter.toSaveFacilitiesDto(saved));
    }

    @Operation(summary = "지도에서 카테고리별로 시설 조회하기, 검색X ✔️🔑- 지도에서 사용", description = "내 주변 1.5km 운동 시설 둘러보기 입니다. 내 좌표와 중심 좌표(=이 지역 재탐색 시 사용 최초는 중심 좌표와 내 좌표 동일), 그리고 카테고리 아이디를 주세요 카테고리는 0이면 전체와 동일")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 아이디, 0이면 전체"),
            @Parameter(name = "x", description = "중심 x"),
            @Parameter(name = "y", description = "중심 y"),
            @Parameter(name = "userX", description = "사용자 X"),
            @Parameter(name = "userY", description = "사용자 Y"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/home/facilities/{categoryId}")
    public ResponseDto<RootApiResponseDto.FacilitiesResponseDto> getFacilities(@PathVariable(name = "categoryId") Integer categoryId, @RequestParam(name = "x") String x, @RequestParam(name = "y")String y, @RequestParam(name = "userX") String userX, @RequestParam(name = "userY")String  userY){
        List<RootApiResponseDto.FacilitiesInfoDto> facilities = rootService.exploreFacilities(categoryId, x, y, userX, userY);
        return ResponseDto.of(RootConverter.toFacilitiesResponseDto(facilities,x,y));
    }

    @Operation(summary = "🚧 운동시설 사진 파일 to AWS S3 Url API, 서버 개발자만 사용함! 🚧", description = "이힣히힣힣 노가다 히힣힣")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @PostMapping(value = "/home/facilities",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<RootApiResponseDto.SaveAsImageUrlDto> saveAsImageUrl(@ModelAttribute RootRequestDto.SaveImageAsUrlDto request) throws IOException
    {
        String s = rootService.saveAsImageUrl(request);
        return ResponseDto.of(RootConverter.toSaveAsImageUrlDto(s));
    }

    @Operation(summary = "지도에서 검색해서 조회하기 ✔️🔑- 지도에서 사용", description = "검색 키워드가 도로명 주소, 주소, 이름에 포함된 시설을 거리순으로 최대 15개 보여줍니다. 지도에서 보기를 눌러 좌표가 변경 될 경우를 대비 하여 중심 좌표를 선택으로 받습니다.")
    @Parameters({
            @Parameter(name = "x", description = "중심 x"),
            @Parameter(name = "y", description = "중심 y"),
            @Parameter(name = "userX", description = "사용자 X"),
            @Parameter(name = "userY", description = "사용자 Y"),
            @Parameter(name = "keyword", description = "검색어"),
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @GetMapping("/home/facilities")
    public ResponseDto<RootApiResponseDto.FacilitiesResponseDto> getFacilities(@RequestParam(name = "x",required = false) String x, @RequestParam(name = "y",required = false)String y, @RequestParam(name = "userX") String userX, @RequestParam(name = "userY")String  userY, @RequestParam(name = "keyword") String keyword){
        List<RootApiResponseDto.FacilitiesInfoDto> facilities = rootService.findFacilities( x, y, userX, userY, keyword);
        return ResponseDto.of(RootConverter.toFacilitiesResponseDto(facilities,x,y));
    }

    @Operation(summary = "추천 검색어 조회 API ✔️ 🔑", description = "추천 검색어 조회 API 입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/home/facilities/keywords")
    public ResponseDto<RootApiResponseDto.FacilitiesKeywordRecommendDto> getRecommend(){
        return ResponseDto.of(RootConverter.toFacilitiesKeywordRecommendDto(rootService.getRecommend()));
    }


    @Operation(summary = "내 알림 허용 여부 확인 API ✔️ 🔑", description = "내 알림 허용 여부 확인 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/home/notification-permit")
    public ResponseDto<RootApiResponseDto.NotificationPermitDto> getNotificationPermit(@AuthUser User user){
        return ResponseDto.of(RootConverter.toNotificationPermitDto(user));
    }

    @Operation(summary = "내 알림 허용 변경 API ✔️ 🔑", description = "내 알림 변경 API입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping("/home/notification-permit")
    public ResponseDto<RootApiResponseDto.NotificationChangeDto> changeNotificationPermit(@RequestBody RootRequestDto.NotificationChangeDto request, @AuthUser User user){
        User changedUser = rootService.changePermit(user, request);
        return ResponseDto.of(RootConverter.toNotificationChangeDto(changedUser));
    }
//
//    @GetMapping("/home/temp")
//    public Integer temp(){
//        Integer test = rootService.test();
//        return test;
//    }
}
