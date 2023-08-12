package fithub.app.service;

import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.web.dto.requestDto.RootRequestDto;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.List;

public interface RootService {

    List<Grade> findAllGrade();

    LevelInfo findLevelInfo();

    List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(Integer categoryId, String x, String y, String keyword,String userX ,String userY);

    String saveAsImageUrl(RootRequestDto.SaveImageAsUrlDto request) throws IOException;

    Integer test();
}
