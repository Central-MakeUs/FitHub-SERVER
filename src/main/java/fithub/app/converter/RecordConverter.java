package fithub.app.converter;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.domain.*;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.RecordHashTag;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecordConverter {

    Logger logger = LoggerFactory.getLogger(RecordConverter.class);

    private final RecordRepository recordRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final AmazonS3Manager amazonS3Manager;

    private static RecordRepository staticRecordRepository;

    private static ExerciseCategoryRepository staticExerciseCategoryRepository;

    private static AmazonS3Manager staticAmazonS3Manager;

    private static Logger staticLogger;

    @PostConstruct
    public void init() {
        staticRecordRepository = this.recordRepository;
        staticExerciseCategoryRepository = this.exerciseCategoryRepository;
        staticAmazonS3Manager = this.amazonS3Manager;
        staticLogger = this.logger;
    }

    public static Record toRecord(RecordRequestDto.CreateRecordDto request, User user, List<HashTag> hashTagList, Integer categoryId) throws IOException
    {
        ExerciseCategory exerciseCategory = staticExerciseCategoryRepository.findById(categoryId).get();
        Record record = Record.builder()
                .exerciseCategory(exerciseCategory)
                .contents(request.getContents())
                .user(user)
                .recordHashTagList(new ArrayList<>())
                .build();

        staticLogger.info("인증 생성 완료");
        record.setRecordHashTagList(toRecordHashTagList(hashTagList, record));
        record.setUser(user);

        //사진 업로드
        MultipartFile recordImage = request.getImage();

        String imageUrl = null;
        if(recordImage != null)
            imageUrl = uploadRecordImage(recordImage, record);
        record.setImage(imageUrl);
        return record;
    }

    public static String uploadRecordImage(MultipartFile recordImage, Record record) throws IOException
    {
        Uuid uuid = staticAmazonS3Manager.createUUID();
        String KeyName = staticAmazonS3Manager.generateRecordKeyName(uuid, recordImage.getOriginalFilename());
        String fileUrl = staticAmazonS3Manager.uploadFile(KeyName, recordImage);
        staticLogger.info("S3에 업로드 한 파일의 url : {}", fileUrl);
        return fileUrl;
    }

    private static List<RecordHashTag> toRecordHashTagList(List<HashTag> hashTagList, Record record){
        return hashTagList.stream()
                .map(hashTag -> {
                    RecordHashTag recordHashTag = RecordHashTag.builder().build();
                    recordHashTag.setRecord(record);
                    recordHashTag.setHashTag(hashTag);
                    return recordHashTag;
                }).collect(Collectors.toList());
    }

    public static RecordResponseDto.RecordSpecDto toRecordSpecDto(Record record, Boolean isLiked){
        return RecordResponseDto.RecordSpecDto.builder()
                .recordId(record.getId())
                .recordCategory(ExerciseCategoryConverter.toCategoryDto(record.getExerciseCategory()))
                .userInfo(UserConverter.toRecordUserDto(record.getUser()))
                .contents(record.getContents())
                .pictureImage(record.getImageUrl())
                .createdAt(record.getCreatedAt())
                .Hashtags(HashTagConverter.toHashtagDtoListRecord(record.getRecordHashTagList()))
                .likes(record.getLikes())
                .isLiked(isLiked)
                .build();
    }

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

    public static RecordResponseDto.recordCreateDto toRecordCreateDto (Record record){
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

    public static RecordResponseDto.recordLikeDto toRecordLikeDto(Record record){
        return RecordResponseDto.recordLikeDto.builder()
                .recordId(record.getId())
                .newLikes(record.getLikes())
                .build();
    }

    public RecordResponseDto.recordScrapDto toRecordScrapDto(Record record, User user){
        return RecordResponseDto.recordScrapDto.builder()
                .recordId(record.getId())
                .userId(user.getId())
                .build();
    }
}
