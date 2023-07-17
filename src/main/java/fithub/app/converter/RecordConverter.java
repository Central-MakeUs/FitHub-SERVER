package fithub.app.converter;

import fithub.app.domain.HashTag;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.repository.RecordRepository;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecordConverter {

    private final RecordRepository recordRepository;
    private static RecordRepository staticRecordRepository;

    @PostConstruct
    public void init() {
        staticRecordRepository = this.recordRepository;
    }

//    public RecordResponseDto.RecordSpecDto toRecordSpecDto(Record record, List<HashTag> hashTagList, Boolean isLiked, Boolean isScraped){
//        return RecordResponseDto.RecordSpecDto.builder()
//                .recordId(record.getId())
//                .recordCategory(ExerciseCategoryConverter.toCategoryDto(record.getExerciseCategory()))
//                .userInfo(UserConverter.toArticleUserDto(record.getUser()))
//                .contents(record.getContents())
//                .pictureUrl(record.getImageUrl())
//                .createdAt(record.getCreatedAt())
//                .Hashtags(HashTagConverter.toHashtagDtoList(hashTagList))
//                .isLiked(isLiked)
//                .isScraped(isScraped)
//                .build();
//    }

    public RecordResponseDto.recordDto toRecordDto(Record record){
        return RecordResponseDto.recordDto.builder()
                .recordId(record.getId())
                .recordCategory(ExerciseCategoryConverter.toCategoryDto(record.getExerciseCategory()))
                .pictureUrl(record.getImageUrl())
                .likes(record.getLikes())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public RecordResponseDto.recordDtoList toRecordDtoList(List<Record> recordList){
        List<RecordResponseDto.recordDto> recordDtoList =
                recordList.stream()
                        .map(record -> toRecordDto(record))
                        .collect(Collectors.toList());

        return RecordResponseDto.recordDtoList.builder()
                .recordList(recordDtoList)
                .size(recordList.size())
                .build();
    }

    public RecordResponseDto.recordCreateDto toRecordCreateDto (Record record){
        return RecordResponseDto.recordCreateDto.builder()
                .recordId(record.getId())
                .ownerId(record.getUser().getId())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public RecordResponseDto.recordUpdateDto toRecordUpdateDto(Record record){
        return RecordResponseDto.recordUpdateDto.builder()
                .recordId(record.getId())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    public RecordResponseDto.recordDeleteDto toRecordDeleteDto(Long id){
        return RecordResponseDto.recordDeleteDto.builder()
                .recordId(id)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public RecordResponseDto.recordDeleteDtoList toRecordDeleteDtoList(List<Long> idList){
        List<RecordResponseDto.recordDeleteDto> recordDeleteDtoList =
                idList.stream()
                        .map(id -> toRecordDeleteDto(id))
                        .collect(Collectors.toList());

        return RecordResponseDto.recordDeleteDtoList.builder()
                .deletedRecordList(recordDeleteDtoList)
                .size(recordDeleteDtoList.size())
                .build();
    }

    public RecordResponseDto.recordLikeDto toRecordLikeDto(Record record, User user){
        return RecordResponseDto.recordLikeDto.builder()
                .recordId(record.getId())
                .userId(user.getId())
                .build();
    }

    public RecordResponseDto.recordScrapDto toRecordScrapDto(Record record, User user){
        return RecordResponseDto.recordScrapDto.builder()
                .recordId(record.getId())
                .userId(user.getId())
                .build();
    }
}
