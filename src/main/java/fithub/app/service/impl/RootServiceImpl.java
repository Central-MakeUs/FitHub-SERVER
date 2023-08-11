package fithub.app.service.impl;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.RecordException;
import fithub.app.converter.RootConverter;
import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.FacilitiesRepository;
import fithub.app.repository.GradeRepository;
import fithub.app.repository.LevelInfoRepository;
import fithub.app.service.RootService;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootServiceImpl implements RootService {

    private final GradeRepository gradeRepository;

    private final LevelInfoRepository levelInfoRepository;

    private final FacilitiesRepository facilitiesRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private Integer maxDistance = 3000;

    @Override
    public List<Grade> findAllGrade() {
        return gradeRepository.findAll();
    }

    @Override
    public LevelInfo findLevelInfo() {
        return levelInfoRepository.findById(1L).get();
    }

    @Override
    public List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(Integer categoryId, String x, String y, String keyword) {

        ExerciseCategory exerciseCategory = null;
        String queryKeyword = null;
        List<Object[]> facilitiesList = null;
        if(categoryId != 0)
            exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new RecordException(Code.CATEGORY_ERROR));
        if(keyword != null)
            queryKeyword = "%" + keyword + "%";

        if(categoryId == 0) {
            if(keyword != null)
                facilitiesList = facilitiesRepository.findFacilitiesAllKeyword(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(x), Float.parseFloat(y), maxDistance, queryKeyword, queryKeyword, queryKeyword);
            else
                facilitiesList = facilitiesRepository.findFacilitiesAll(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(x), Float.parseFloat(y), maxDistance);
        }
        else {
            if(keyword != null)
                facilitiesList = facilitiesRepository.findFacilitiesCategory(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(x), Float.parseFloat(y), maxDistance, categoryId, queryKeyword, queryKeyword, queryKeyword);
            else
                facilitiesList = facilitiesRepository.findFacilitiesCategoryAll(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(x), Float.parseFloat(y), maxDistance,categoryId);
        }

        List<RootApiResponseDto.FacilitiesInfoDto> facilitiesInfoDtoList = facilitiesList.stream()
                .map(facilities -> RootConverter.toFacilitiesInfoDto(facilities)).collect(Collectors.toList());

        return facilitiesInfoDtoList;
    }
}
