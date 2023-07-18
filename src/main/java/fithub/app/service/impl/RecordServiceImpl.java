package fithub.app.service.impl;

import fithub.app.converter.HashTagConverter;
import fithub.app.converter.RecordConverter;
import fithub.app.domain.HashTag;
import fithub.app.domain.Record;
import fithub.app.domain.User;
import fithub.app.repository.HashTagRepository;
import fithub.app.repository.RecordRepositories.RecordLikesRepository;
import fithub.app.repository.RecordRepositories.RecordRepository;
import fithub.app.service.RecordService;
import fithub.app.web.dto.requestDto.RecordRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    private final RecordLikesRepository recordLikesRepository;

    private final HashTagRepository hashTagRepository;

    @Override
    @Transactional(readOnly = false)
    public Record create(RecordRequestDto.CreateRecordDto request, User user, Integer categoryId) throws IOException
    {
        String exerciseTag = request.getExerciseTag();
        HashTag exerciseHash = hashTagRepository.findByName(exerciseTag).orElseGet(() -> HashTagConverter.newHashTag(exerciseTag));
        List<HashTag> hashTagList = request.getTagList().stream()
                .map(tag -> hashTagRepository.findByName(tag).orElseGet(()-> HashTagConverter.newHashTag(tag)))
                .collect(Collectors.toList());

        hashTagList.add(exerciseHash);
        Record record = RecordConverter.toRecord(request, user, hashTagList, categoryId);
        return recordRepository.save(record);
    }
}
