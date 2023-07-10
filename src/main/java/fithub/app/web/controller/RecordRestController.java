package fithub.app.web.controller;

import fithub.app.auth.handler.annotation.AuthUser;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecordRestController {

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

    @PostMapping("/record")
    public ResponseEntity<RecordResponseDto.recordCreateDto> createRecord(@RequestBody RecordRequestDto.uploadRecordDto request, @AuthUser User user){
        return null;
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
