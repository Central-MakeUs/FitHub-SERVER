package fithub.app.service;

import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.web.dto.responseDto.RootApiResponseDto;

import java.util.List;

public interface RootService {

    List<Grade> findAllGrade();

    LevelInfo findLevelInfo();

    List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(Integer categoryId, String x, String y, String keyword,String userX ,String userY);
}
