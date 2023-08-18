package fithub.app.service;

import fithub.app.domain.Grade;
import fithub.app.domain.LevelInfo;
import fithub.app.domain.RecommendFacilitiesKeyword;
import fithub.app.domain.User;
import fithub.app.web.dto.requestDto.RootRequestDto;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.List;

public interface RootService {

    List<Grade> findAllGrade();

    LevelInfo findLevelInfo();

    List<RootApiResponseDto.FacilitiesInfoDto> exploreFacilities(Integer categoryId, String x, String y, String userX ,String userY);

//    List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(String x, String y, String userX ,String userY,String keyword);
    List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(String x, String y, String userX ,String userY,String keyword, Integer categoryId);

    String saveAsImageUrl(RootRequestDto.SaveImageAsUrlDto request) throws IOException;

    Integer test();

    User changePermit(User user, RootRequestDto.NotificationChangeDto request);

    List<RecommendFacilitiesKeyword> getRecommend();
}
