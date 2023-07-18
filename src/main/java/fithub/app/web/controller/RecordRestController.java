package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.base.ResponseDto;
import fithub.app.converter.RecordConverter;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.service.RecordService;
import fithub.app.validation.annotation.ExistCategory;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import io.swagger.models.auth.In;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
public class RecordRestController {

    Logger logger = LoggerFactory.getLogger(RecordRestController.class);

    private final RecordService recordService;


    @GetMapping("/record/{id}")
    public ResponseEntity<RecordResponseDto.recordDto> recordSpec(@PathVariable Long id){
        return null;
    }

    @GetMapping("/records")
    public ResponseEntity<RecordResponseDto.recordDtoList> recordTimeList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/records/likes")
    public ResponseEntity<RecordResponseDto.recordDtoList> recordLikesList(@RequestParam Long last){
        return null;
    }

    @GetMapping("/records/{categoryId}")
    public ResponseEntity<RecordResponseDto.recordDtoList> recordCategoryTimeList(@PathVariable Long categoryId, @RequestParam Long last){
        return null;
    }

    @GetMapping("/records/{categoryId}/likes")
    public ResponseEntity<RecordResponseDto.recordDtoList> recordCategoryLikesList(@PathVariable Long categoryId, @RequestParam Long last){
        return null;
    }

    @Operation(summary = "운동인증 작성 API", description = "운동인증 작성 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK : 정상응답"),
            @ApiResponse(responseCode = "4031", description = "NOT_FOUND : 게시글이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "5000", description = "Server Error : 똘이에게 알려주세요",content =@Content(schema =  @Schema(implementation = ResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "user", hidden = true)
    })
    @PostMapping(value = "/records/{categoryId}",consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseDto<RecordResponseDto.recordCreateDto> createRecord(@PathVariable(name = "categoryId") @ExistCategory Integer categoryId, @ModelAttribute @Valid RecordRequestDto.CreateRecordDto request, @AuthUser User user) throws IOException
    {

        logger.info("사용자가 준 정보 : {}", request.toString());

        Record record = recordService.create(request, user, categoryId);
        return ResponseDto.of(RecordConverter.toRecordCreateDto(record));
    }

    @PatchMapping("/record/{id}")
    public ResponseEntity<RecordResponseDto.recordUpdateDto> updateRecord(@PathVariable Long id, @RequestBody RecordRequestDto.updateRecordDto request, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/record/{id}")
    public ResponseEntity<RecordResponseDto.recordDeleteDto> deleteRecord(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @DeleteMapping("/records")
    public ResponseEntity<RecordResponseDto.recordDeleteDtoList> deleteListRecord(@RequestBody RecordRequestDto.deleteListRecordDto request, @AuthUser User user){
        return null;
    }

    @PostMapping("/record/{id}/likes")
    public ResponseEntity<RecordResponseDto.recordLikeDto> likeRecord(@PathVariable Long id, @AuthUser User user){
        return null;
    }

    @PostMapping("/record/{id}/scrap")
    public ResponseEntity<RecordResponseDto.recordScrapDto> scrapRecord(@PathVariable Long id, @AuthUser User user){
        return null;
    }
}
