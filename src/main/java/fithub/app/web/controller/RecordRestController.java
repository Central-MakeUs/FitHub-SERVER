package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.converter.UserConverter;
import fithub.app.domain.Article;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.service.RecordService;
import fithub.app.validation.annotation.ExistCategory;
import fithub.app.validation.annotation.ExistRecord;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import fithub.app.web.dto.responseDto.UserResponseDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "운동 인증 API", description = "운동 인증 관련 API")
public class RecordRestController {

    Logger logger = LoggerFactory.getLogger(RecordRestController.class);

    private final RecordService recordService;

    @Operation(summary = "운동인증 상세조회 API ✔️ 🔑", description = "운동인증 상세조회 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "recordId", description = "조회하려는 인증글의 Id(목록 조회 시 서버가 줄거임)")
    })
    @GetMapping("/records/{recordId}/spec")
    public ResponseDto<RecordResponseDto.RecordSpecDto> recordSpec(@PathVariable(name = "recordId") @ExistRecord Long recordId, @AuthUser User user){

        Record record = recordService.getRecord(recordId);
        return ResponseDto.of(RecordConverter.toRecordSpecDto(record, user));
    }

    @Operation(summary = "운동 인증 목록 조회 API - 최신순 ✔️ 🔑", description = "운동 인증 목록 조회 API 입니다. categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "운동 카테고리, 0이면 전체 조회"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠")
    })
    @GetMapping("/records/{categoryId}")
    public ResponseDto<RecordResponseDto.recordDtoList> recordTimeList(@RequestParam(name = "pageIndex") Integer pageIndex, @PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @AuthUser User user){
        Page<Record> records = null;
        if (categoryId != 0)
            records = recordService.findRecordPagingCategoryAndCreatedAt(user, categoryId, pageIndex);
        else
            records = recordService.findRecordPagingCreatedAt(user,pageIndex);
        return ResponseDto.of(RecordConverter.toRecordDtoList(records, user));
    }

    @Operation(summary = "운동 인증 목록 조회 API - 인기순 ✔️ 🔑", description = "운동 인증 목록 조회 API 입니다. categoryId를 0으로 주면 카테고리 무관 전체 조회, pageIndex를 queryString으로 줘서 페이징 사이즈는 12개 ❗주의, 첫 페이지는 0번 입니다 아시겠죠?❗")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "운동 카테고리, 0이면 전체 조회"),
            @Parameter(name = "pageIndex", description = "페이지 번호, 필수인데 안 주면 0번 페이지로 간주하게 해둠")
    })
    @GetMapping("/records/{categoryId}likes")
    public ResponseDto<RecordResponseDto.recordDtoList> recordLikesList(@RequestParam(name = "pageIndex") Integer pageIndex, @PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @AuthUser User user){
        Page<Record> records = null;
        if (categoryId != 0)
            records = recordService.findRecordPagingCategoryAndLikes(user, categoryId, pageIndex);
        else
            records = recordService.findRecordPagingLikes(user,pageIndex);
        return ResponseDto.of(RecordConverter.toRecordDtoList(records, user));
    }

    @Operation(summary = "운동인증 작성 API ✔️ 🔑- 홈 페이지 작업 후 수정 필요", description = "운동인증 작성 API 입니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "categoryId", description = "운동 카테고리"),
    })
    @PostMapping(value = "/records/{categoryId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<RecordResponseDto.recordCreateDto> createRecord(@PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @ModelAttribute @Valid RecordRequestDto.CreateRecordDto request, @AuthUser User user) throws IOException
    {

        logger.info("사용자가 준 정보 : {}", request.toString());

        Record record = recordService.create(request, user, categoryId);
        // 레벨 계산
        recordService.calcExp(user,categoryId);
        return ResponseDto.of(RecordConverter.toRecordCreateDto(record));
    }

    @Operation(summary = "운동인증 수정 API ✔️ 🔑", description = "운동인증 수정 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4030", description = "BAD_REQUEST : 카테고리가 잘못되었습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4042", description = "FORBIDDEN : 다른 사람의 운동인증", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "recordId", description = "운동 인증 아이디"),
    })
    @PatchMapping(value = "/record/{recordId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<RecordResponseDto.recordUpdateDto> updateRecord(@PathVariable(name = "recordId") Long recordId, @ModelAttribute RecordRequestDto.updateRecordDto request, @AuthUser User user) throws IOException
    {
        logger.error("사용자가 넘긴 정보 : {}",request.toString());
        Record record = recordService.updateRecord(request, recordId, user);
        return ResponseDto.of(RecordConverter.toRecordUpdateDto(record));
    }


    @Operation(summary = "운동인증 삭제 API✔️ 🔑", description = "운동인증 삭제 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4042", description = "FORBIDDEN : 다른 사람의 운동인증", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "recordId", description = "운동 인증 아이디"),
    })
    @DeleteMapping("/record/{recordId}")
    public ResponseDto<RecordResponseDto.recordDeleteDto> deleteRecord(@PathVariable(name = "recordId") Long recordId, @AuthUser User user){
        recordService.deleteRecordSingle(recordId,user);
        return ResponseDto.of(RecordConverter.toRecordDeleteDto(recordId));
    }

    @Operation(summary = "운동인증 한번에 여러개 삭제 API ✔️🔑- 마이 페이지에서 사용됨", description = "운동인증 한번에 여러개 삭제 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4042", description = "FORBIDDEN : 다른 사람의 운동인증이 하나 라도 있을 때", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @PatchMapping("/records")
    public ResponseDto<RecordResponseDto.recordDeleteDtoList> deleteListRecord(@RequestBody RecordRequestDto.deleteListRecordDto request, @AuthUser User user){
        recordService.deleteRecordBulk(request, user);
        return ResponseDto.of(RecordConverter.toRecordDeleteDtoList(request.getRecordIdList()));
    }

    @Operation(summary = "운동인증 좋아요 누르기/취소 ✔️ 🔑",description = "좋아요를 누른 적이 있다면 취소, 없다면 좋아요 누르기 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답, 성공 시 새로 바뀐 좋아요 갯수 응답에 포함"),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "recordId", description = "운동 인증 아이디"),
    })
    @PostMapping("/records/{recordId}/likes")
    public ResponseDto<RecordResponseDto.recordLikeDto> likeRecord(@PathVariable(name = "recordId") @ExistRecord Long recordId, @AuthUser User user) throws IOException
    {
        Record record = recordService.toggleRecordLike(recordId, user);

        // 알림 보내기
        if(user.isLikedRecord(record) && record.getUser().getCommunityPermit() && !record.getUser().getId().equals(user.getId()))
            recordService.alarmRecordLike(record,user);
        return ResponseDto.of(RecordConverter.toRecordLikeDto(record, user));
    }

    @Operation(summary = "운동 인증 신고하기 ✔️🔑",description = "운동 인증을 신고하는 API이며 이미 신고한 경우는 안된다고 응답이 갑니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4041", description = "NOT_FOUND : 운동 인증이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4061", description = "BAD_REQUEST : 이미 신고 했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4062", description = "BAD_REQUEST : 자신의 콘텐츠는 신고가 안됩니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
            @Parameter(name = "recordId", description = "운동인증 아이디")
    })
    @PostMapping("/records/{recordId}/report")
    public ResponseDto<RecordResponseDto.RecordReportDto> reportRecord(@PathVariable(name = "recordId")Long recordId, @AuthUser User user){
        ContentsReport contentsReport = recordService.reportRecord(recordId, user);
        return ResponseDto.of(RecordConverter.toRecordReportDto(recordId, contentsReport));
    }

    @Operation(summary = "오늘 적은 운동인증이 있는지 체크하는 API ✔️🔑",description = "오늘 적은 운동인증이 있는지 체크하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true),
    })
    @GetMapping("/records/check-today")
    public ResponseDto<RecordResponseDto.RecordLimitDto> checkRecordLimit(@AuthUser User user){
        Boolean isWrite = recordService.checkWriteRecord(user);
        return ResponseDto.of(RecordConverter.toRecordLimitDto(isWrite));
    }
}
