package fithub.app.web.controller;

import fithub.app.base.ResponseDto;
import fithub.app.firebase.service.FireBaseService;
import fithub.app.web.dto.requestDto.ArticleRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "알람 API", description = "푸쉬 알람 API, 임시!!!!!!")
@RestController
@RequiredArgsConstructor
public class AlarmRestController {

    private final FireBaseService fireBaseService;

    @Operation(summary = "<테스트용> 알림 보내보기 API V1 - notification + data ✔️🔑",description = "테스트임다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PostMapping("/test-alarm/v1")
    public String testFCMV1 (@RequestBody ArticleRequestDto.ArticleLikeAlarmDto request) throws IOException
    {
        fireBaseService.sendMessageToV1(request.getToken(),"test","test","자결","자결");
        return null;
    }


    @Operation(summary = "<테스트용> 알림 보내보기 API V2 - data  (notification은 모양 조금이라도 다르면 안됨) ✔️🔑",description = "테스트임다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PostMapping("/test-alarm/v2")
    public String testFCMV3(@RequestBody ArticleRequestDto.ArticleLikeAlarmDto request) throws IOException
    {
        fireBaseService.sendMessageToV3(request.getToken(),"test","test","자결","자결");
        return null;
    }
}
