package fithub.app.service.impl;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.ArticleException;
import fithub.app.base.exception.handler.RecordException;
import fithub.app.converter.ArticleConverter;
import fithub.app.converter.HashTagConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.domain.*;
import fithub.app.domain.mapping.RecordHashTag;
import fithub.app.domain.mapping.RecordLikes;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.GradeRepository;
import fithub.app.repository.HashTagRepositories.HashTagRepository;

import fithub.app.repository.HashTagRepositories.RecordHashTagRepository;
import fithub.app.repository.RecordRepositories.RecordLikesRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.repository.UserExerciseRepository;
import fithub.app.service.RecordService;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {

    Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    private final RecordRepository recordRepository;

    private final RecordLikesRepository recordLikesRepository;

    private final HashTagRepository hashTagRepository;

    private final RecordHashTagRepository recordHashTagRepository;

    private final AmazonS3Manager amazonS3Manager;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final UserExerciseRepository userExerciseRepository;

    private final GradeRepository gradeRepository;

    @Value("${paging.size}")
    Integer size;

    @Value("${recordExp.combo}")
    Integer comboExp;

    @Value("${recordExp.default}")
    Integer defaultExp;
    @Override
    @Transactional(readOnly = false)
    public Record create(RecordRequestDto.CreateRecordDto request, User user, Integer categoryId) throws IOException
    {
        String exerciseTag = request.getExerciseTag();
        HashTag exerciseHash = hashTagRepository.findByName(exerciseTag).orElseGet(() -> HashTagConverter.newHashTag(exerciseTag));
        List<HashTag> hashTagList = new ArrayList<>();
        if (request.getHashTagList() != null) {
            hashTagList =request.getHashTagList().stream()
                    .map(tag -> hashTagRepository.findByName(tag).orElseGet(() -> HashTagConverter.newHashTag(tag)))
                    .collect(Collectors.toList());
        }
        hashTagList.add(exerciseHash);
        Record record = RecordConverter.toRecord(request, user, hashTagList, categoryId);
        return recordRepository.save(record);
    }

    @Override
    public Record getRecord(Long recordId) {
        return recordRepository.findById(recordId).orElseThrow(()->new RecordException(Code.RECORD_NOT_FOUND));
    }

    @Override
    public Boolean getIsLiked(Record record, User user) {
        Optional<RecordLikes> isLiked = recordLikesRepository.findByRecordAndUser(record, user);
        return isLiked.isPresent();
    }

    @Override
    @Transactional(readOnly = false)
    public Record toggleRecordLike(Long recordId, User user) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new RecordException(Code.RECORD_NOT_FOUND));
        Optional<RecordLikes> recordLike = recordLikesRepository.findByRecordAndUser(record, user);

        Record updatedRecord = null;

        if(recordLike.isPresent()){
            recordLike.get().getUser().getRecordLikesList().remove(recordLike.get());
            recordLikesRepository.delete(recordLike.get());
            updatedRecord = record.likeToggle(false);
        }else{
            updatedRecord = record.likeToggle(true);
            RecordLikes recordLikes = RecordLikes.builder()
                    .record(record)
                    .user(user)
                    .build();
            recordLikes.setUser(user);
            recordLikesRepository.save(recordLikes);
        }

        return updatedRecord;
    }

    @Override
    @Transactional(readOnly = false)
    public Record updateRecord(RecordRequestDto.updateRecordDto request, Long recordId, User user) throws IOException {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new RecordException(Code.RECORD_NOT_FOUND));

        if(!record.getUser().getId().equals(user.getId()))
            throw new RecordException(Code.RECORD_FORBIDDEN);

        String imageUrl = record.getImageUrl();


        if(request.getRemainImageUrl() == null){
            String Keyname = ArticleConverter.toKeyName(imageUrl);
            amazonS3Manager.deleteFile(Keyname.substring(1));
        }

        // 필요 없어진 사진은 지웠으니 이제 게시글과 연결된 해시태그 재 설정
        List<RecordHashTag> recordHashTagList = record.getRecordHashTagList();

        for(int i = 0; i < recordHashTagList.size(); i++) {
            RecordHashTag articleHashTag = recordHashTagList.get(i);
            record.getRecordHashTagList().remove(articleHashTag);
            recordHashTagRepository.delete(articleHashTag);
        }

        if(recordHashTagList.size() > 0) {
            RecordHashTag last = recordHashTagList.get(0);
            recordHashTagList.remove(last);
            recordHashTagRepository.delete(last);
        }

        String exerciseTag =  request.getExerciseTag();
        HashTag exercisehashTag = hashTagRepository.findByName(exerciseTag).orElseGet(() -> HashTagConverter.newHashTag(exerciseTag));

        List<HashTag> hashTagList = request.getHashTagList().stream()
                .map(tag -> hashTagRepository.findByName(tag).orElseGet(()-> HashTagConverter.newHashTag(tag)))
                .collect(Collectors.toList());

        hashTagList.add(exercisehashTag);

        return RecordConverter.toUpdateRecord(record,request,hashTagList);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRecordSingle(Long recordId, User user) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new RecordException(Code.RECORD_NOT_FOUND));

        if(!record.getUser().getId().equals(user.getId()))
            throw new RecordException(Code.RECORD_FORBIDDEN);
        String imageUrl = record.getImageUrl();
        if (imageUrl != null) {
            String Keyname = ArticleConverter.toKeyName(imageUrl);
            amazonS3Manager.deleteFile(Keyname.substring(1));
        }

        List<RecordHashTag> recordHashTagList = record.getRecordHashTagList();
        for(int i = 0; i < recordHashTagList.size(); i++) {
            RecordHashTag recordHashTag = recordHashTagList.get(i);
            record.getRecordHashTagList().remove(recordHashTag);
            recordHashTagRepository.delete(recordHashTag);
        }

        if(recordHashTagList.size() > 0) {
            RecordHashTag last = recordHashTagList.get(0);
            recordHashTagList.remove(last);
            recordHashTagRepository.delete(last);
        }

        recordRepository.delete(record);
    }

    @Override
    public Page<Record> findRecordPagingCategoryAndCreatedAt(User user, Integer categoryId, Long last) {
        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));

        Page<Record> findRecord = null;

        if(last == null)
            last = 0L;
        Optional<Record> lastRecord = recordRepository.findById(last);

        if (lastRecord.isPresent())
            findRecord = recordRepository.findByCreatedAtLessThanAndExerciseCategoryOrderByCreatedAtDesc(lastRecord.get().getCreatedAt(), exerciseCategory, PageRequest.of(0, size));
        else
            findRecord = recordRepository.findAllByExerciseCategoryOrderByCreatedAtDesc(exerciseCategory,PageRequest.of(0, size));
        return findRecord;
    }

    @Override
    public Page<Record> findRecordPagingCreatedAt(User user, Long last) {
        Page<Record> findRecord = null;

        if(last == null)
            last = 0L;
        Optional<Record> lastRecord = recordRepository.findById(last);

        if(lastRecord.isPresent())
            findRecord = recordRepository.findByCreatedAtLessThanOrderByCreatedAtDesc(lastRecord.get().getCreatedAt(),PageRequest.of(0, size));
        else
            findRecord = recordRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, size));
        return findRecord;
    }

    @Override
    public Page<Record> findRecordPagingCategoryAndLikes(User user, Integer categoryId, Long last) {
        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new ArticleException(Code.CATEGORY_ERROR));

        Page<Record> findRecord = null;

        if(last == null)
            last = 0L;
        Optional<Record> lastRecord = recordRepository.findById(last);

        if (lastRecord.isPresent())
            findRecord = recordRepository.findByLikesLessThanAndExerciseCategoryOrderByLikesDesc(lastRecord.get().getLikes(), exerciseCategory, PageRequest.of(0, size));
        else
            findRecord = recordRepository.findAllByExerciseCategoryOrderByLikesDesc(exerciseCategory,PageRequest.of(0, size));
        return findRecord;
    }

    @Override
    public Page<Record> findRecordPagingLikes(User user, Long last) {
        Page<Record> findRecord = null;

        if(last == null)
            last = 0L;
        Optional<Record> lastRecord = recordRepository.findById(last);

        if(lastRecord.isPresent())
            findRecord = recordRepository.findByLikesLessThanOrderByLikesDesc(lastRecord.get().getLikes(),PageRequest.of(0, size));
        else
            findRecord = recordRepository.findAllByOrderByLikesDesc(PageRequest.of(0, size));
        return findRecord;
    }

    @Override
    @Transactional(readOnly = false)
    public void calcExp(User user, Integer categoryId) {

        // 카테고리에 해당하는 유저의 운동 종목을 가져온다
        UserExercise userExercise = userExerciseRepository.findByUserAndExerciseCategory(user, exerciseCategoryRepository.findById(categoryId).get()).get();
        Grade exerciseGrade = gradeRepository.findByName(userExercise.getGrade().getName()).get();

        // 경험치 계산을 위해 먼저 연속 인증인지 판단한다.
        LocalDate recentRecord = userExercise.getRecentRecord();

        Integer newExp = 0;

        // 연속 일수와 경험치 계산
        if(recentRecord == null || ChronoUnit.DAYS.between(recentRecord, LocalDate.now()) >= 2) {
            userExercise.setContiguousDay(1);
            newExp = defaultExp;
        }
        else{
            Integer comboMultiple = Math.min(userExercise.getContiguousDay() + 1, exerciseGrade.getMaxContiguous());
            userExercise.setContiguousDay(comboMultiple);
            newExp = defaultExp + comboExp * comboMultiple;
        }

        // 경험치 계산 및 레벨업, 남은 경험치 이월 및 인증 수 업데이트
        int totalExp = userExercise.getExp() + newExp;

        // 레벨 업
        if (totalExp >= exerciseGrade.getMaxExp()) {
            totalExp -= exerciseGrade.getMaxExp();
            userExercise.setGrade(gradeRepository.findByLevel(exerciseGrade.getLevel() + 1).get());
        }
        userExercise.setExp(totalExp);
        userExercise.setRecords(userExercise.getRecords() + 1);
        userExercise.setRecentRecord(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteRecordBulk(RecordRequestDto.deleteListRecordDto request, User user) {
        List<Record> recordList = recordRepository.findByIdIn(request.getRecordIdList());
        for (Record record : recordList) {
            if (!record.getUser().getId().equals(user.getId()))
                throw new RecordException(Code.RECORD_FORBIDDEN);

            String imageUrl = record.getImageUrl();
            if (imageUrl != null) {
                String Keyname = ArticleConverter.toKeyName(imageUrl);
                amazonS3Manager.deleteFile(Keyname.substring(1));
            }
            recordRepository.delete(record);
        }
    }
}
