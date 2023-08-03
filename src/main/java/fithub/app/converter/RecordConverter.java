package fithub.app.converter;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.domain.*;
import fithub.app.domain.mapping.ArticleHashTag;
import fithub.app.domain.mapping.ContentsReport;
import fithub.app.domain.mapping.RecordHashTag;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import fithub.app.web.dto.responseDto.RecordResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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

    public static Record toUpdateRecord(Record record, RecordRequestDto.updateRecordDto request, List<HashTag> hashTagList) throws IOException{
        ExerciseCategory exerciseCategory = staticExerciseCategoryRepository.findById(request.getCategory()).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));
        record.update(request, exerciseCategory);
        boolean equals = request.getRemainImageUrl().equals("");
        staticLogger.info("컨버터 사진 로그 : 새로 올린 사진이 있는가? : {}", request.getNewImage());
        staticLogger.info("컨버터 사진 로그 : 그대로 둘 사진이 있는가? : {}", request.getRemainImageUrl());
        if (request.getRemainImageUrl() != null && !request.getRemainImageUrl().equals(""))
            record.setImage(request.getRemainImageUrl());
        else{
            MultipartFile recordImage = request.getNewImage();
            String imageUrl = null;
            if(recordImage != null)
                imageUrl = uploadRecordImage(recordImage, record);
            record.setImage(imageUrl);
        }
        record.setRecordHashTagList(toRecordHashTagList(hashTagList, record));
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

    public static RecordResponseDto.RecordSpecDto toRecordSpecDto(Record record, Boolean isLiked, User user){
        return RecordResponseDto.RecordSpecDto.builder()
                .recordId(record.getId())
                .recordCategory(ExerciseCategoryConverter.toCategoryDto(record.getExerciseCategory()))
                .loginUserProfileUrl(user.getProfileUrl())
                .userInfo(UserConverter.toCommunityUserInfo(record.getUser()))
                .contents(record.getContents())
                .pictureImage(record.getImageUrl())
                .comments(record.getComments())
                .createdAt(record.getCreatedAt())
                .Hashtags(HashTagConverter.toHashtagDtoListRecord(record.getRecordHashTagList()))
                .likes(record.getLikes())
                .isLiked(isLiked)
                .build();
    }

    public static RecordResponseDto.recordDto toRecordDto(Record record, User user){
        return RecordResponseDto.recordDto.builder()
                .recordId(record.getId())
                .pictureUrl(record.getImageUrl())
                .likes(record.getLikes())
                .isLiked(user.isLikedRecord(record))
                .createdAt(record.getCreatedAt())
                .build();
    }

    public static RecordResponseDto.recordDtoList toRecordDtoList(Page<Record> records, User user){
        List<Record> recordList = records.toList();
        List<RecordResponseDto.recordDto> recordDtoList =
                recordList.stream()
                        .map(record -> toRecordDto(record, user))
                        .collect(Collectors.toList());

        return RecordResponseDto.recordDtoList.builder()
                .recordList(recordDtoList)
                .totalElements(records.getTotalElements())
                .totalPage(records.getTotalPages())
                .listSize(recordList.size())
                .isFirst(records.isFirst())
                .isLast(records.isLast())
                .build();
    }

    public static RecordResponseDto.recordCreateDto toRecordCreateDto (Record record){
        return RecordResponseDto.recordCreateDto.builder()
                .recordId(record.getId())
                .ownerId(record.getUser().getId())
                .createdAt(record.getCreatedAt())
                .build();
    }

    public static RecordResponseDto.recordUpdateDto toRecordUpdateDto(Record record){
        return RecordResponseDto.recordUpdateDto.builder()
                .recordId(record.getId())
                .updatedAt(record.getUpdatedAt())
                .build();
    }

    public static RecordResponseDto.recordDeleteDto toRecordDeleteDto(Long id){
        return RecordResponseDto.recordDeleteDto.builder()
                .recordId(id)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public static RecordResponseDto.recordDeleteDtoList toRecordDeleteDtoList(List<Long> idList){
        List<RecordResponseDto.recordDeleteDto> recordDeleteDtoList =
                idList.stream()
                        .map(id -> toRecordDeleteDto(id))
                        .collect(Collectors.toList());

        return RecordResponseDto.recordDeleteDtoList.builder()
                .deletedRecordList(recordDeleteDtoList)
                .size(recordDeleteDtoList.size())
                .build();
    }

    public static RecordResponseDto.recordLikeDto toRecordLikeDto(Record record, User user){
        return RecordResponseDto.recordLikeDto.builder()
                .recordId(record.getId())
                .newLikes(record.getLikes())
                .isLiked(user.isLikedRecord(record))
                .build();
    }

    public static RecordResponseDto.RecordReportDto toRecordReportDto(Long recordId, ContentsReport contentsReport){
        return RecordResponseDto.RecordReportDto.builder()
                .reportedRecordId(recordId)
                .reportedAt(LocalDateTime.now())
                .build();
    }
}
