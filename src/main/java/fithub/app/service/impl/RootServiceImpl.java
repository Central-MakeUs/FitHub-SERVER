package fithub.app.service.impl;

import fithub.app.aws.s3.AmazonS3Manager;
import fithub.app.base.Code;
import fithub.app.base.exception.handler.RecordException;
import fithub.app.base.exception.handler.RootException;
import fithub.app.converter.ArticleImageConverter;
import fithub.app.converter.RootConverter;
import fithub.app.domain.*;
import fithub.app.feign.kakaoLocal.KakaoLocalConverter;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalParam;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResponseDto;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResultDto;
import fithub.app.feign.kakaoLocal.service.KakaoLocalFeign;
import fithub.app.repository.*;
import fithub.app.service.RootService;
import fithub.app.web.dto.requestDto.RootRequestDto;
import fithub.app.web.dto.responseDto.RootApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RootServiceImpl implements RootService {

    private final GradeRepository gradeRepository;

    private final LevelInfoRepository levelInfoRepository;

    private final FacilitiesRepository facilitiesRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    private final AmazonS3Manager amazonS3Manager;

    private final RecommendFacilitiesKeywordRepository recommendFacilitiesKeywordRepository;

    private Integer maxDistance = 1500;

    @Value("${kakao.localApiKey}")
    private String kakaoLocalApiKey;

    @Value("${kakao.centerX}")
    private String centerX;

    @Value("${kakao.centerY}")
    private String centerY;

    @Value("${kakao.radius}")
    private Integer radius;
    private String x;

    private String y;

    private final KakaoLocalFeign kakaoLocalFeign;

    private final TermsRepository termsRepository;

    @Override
    public List<Grade> findAllGrade() {
        return gradeRepository.findAll();
    }

    @Override
    public LevelInfo findLevelInfo() {
        return levelInfoRepository.findById(1L).get();
    }

    @Override
    public List<RootApiResponseDto.FacilitiesInfoDto> exploreFacilities(Integer categoryId, String x, String y, String userX, String userY) {

        ExerciseCategory exerciseCategory = null;
        String queryKeyword = null;
        List<Object[]> facilitiesList = null;
        if(categoryId != 0)
            exerciseCategory = exerciseCategoryRepository.findById(categoryId).orElseThrow(() -> new RecordException(Code.CATEGORY_ERROR));

        if(categoryId == 0) {
            facilitiesList = facilitiesRepository.findFacilitiesAll(Float.parseFloat(userX), Float.parseFloat(userY), Float.parseFloat(x), Float.parseFloat(y), maxDistance);
        }
        else {
            facilitiesList = facilitiesRepository.findFacilitiesCategoryAll(Float.parseFloat(userX), Float.parseFloat(userY), Float.parseFloat(x), Float.parseFloat(y), maxDistance,categoryId);
        }

        List<RootApiResponseDto.FacilitiesInfoDto> facilitiesInfoDtoList = facilitiesList.stream()
                .map(facilities -> RootConverter.toFacilitiesInfoDto(facilities)).collect(Collectors.toList());

        return facilitiesInfoDtoList;
    }

//    @Override
//    public List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(String x, String y, String userX, String userY, String keyword) {
//
//
//        String queryKeyword = null;
//        List<Object[]> facilitiesList = null;
//
//        queryKeyword = "%" + keyword + "%";
//
//        if((x == null || x.equals(""))  && (y == null || y.equals("")))
//            facilitiesList = facilitiesRepository.findFacilitiesMyLoc(Float.parseFloat(userX), Float.parseFloat(userY),queryKeyword, queryKeyword, queryKeyword,Float.parseFloat(userX), Float.parseFloat(userY));
//        else
//            facilitiesList = facilitiesRepository.findFacilities(Float.parseFloat(userX), Float.parseFloat(userY), queryKeyword, queryKeyword, queryKeyword, Float.parseFloat(x), Float.parseFloat(y));
//
//
//        List<RootApiResponseDto.FacilitiesInfoDto> facilitiesInfoDtoList = facilitiesList.stream()
//                .map(facilities -> RootConverter.toFacilitiesInfoDto(facilities)).collect(Collectors.toList());
//
//        return facilitiesInfoDtoList;
//    }

    @Override
    public List<RootApiResponseDto.FacilitiesInfoDto> findFacilities(String x, String y, String userX, String userY, String keyword, Integer categoryId) {

        if(categoryId != 0)
            exerciseCategoryRepository.findById(categoryId).orElseThrow(()->new RootException(Code.CATEGORY_ERROR));

        String queryKeyword = null;
        List<Object[]> facilitiesList = null;

        queryKeyword = keyword == null ? null :  "%" + keyword + "%";

        if(queryKeyword == null) {
            if (categoryId == 0)
                facilitiesList = facilitiesRepository.findFacilities(Float.parseFloat(userX), Float.parseFloat(userY), Float.parseFloat(x), Float.parseFloat(y), maxDistance);
            else
                facilitiesList = facilitiesRepository.findFacilitiesCategory(Float.parseFloat(userX), Float.parseFloat(userY), Float.parseFloat(x), Float.parseFloat(y), maxDistance, categoryId);
        }
        else {
            if(categoryId == 0)
                facilitiesList = facilitiesRepository.findFacilitiesKeyword(Float.parseFloat(userX), Float.parseFloat(userY), queryKeyword, queryKeyword, queryKeyword, Float.parseFloat(x), Float.parseFloat(y), maxDistance);
            else
                facilitiesList = facilitiesRepository.findFacilitiesKeywordCategory(Float.parseFloat(userX), Float.parseFloat(userY), queryKeyword, queryKeyword, queryKeyword, Float.parseFloat(x), Float.parseFloat(y), maxDistance, categoryId);
        }


        List<RootApiResponseDto.FacilitiesInfoDto> facilitiesInfoDtoList = facilitiesList.stream()
                .map(facilities -> RootConverter.toFacilitiesInfoDto(facilities)).collect(Collectors.toList());

        return facilitiesInfoDtoList;
    }

    @Override
    public List<RootApiResponseDto.FacilitiesInfoKeywordDto> findFacilitiesKeyword(String userX, String userY, String keyword) {
        String queryKeyword = null;
        List<Object[]> facilitiesList = null;

        queryKeyword = keyword == null ? null :  "%" + keyword + "%";
        facilitiesList = facilitiesRepository.findFacilitiesByKeyword(Float.parseFloat(userX),Float.parseFloat(userY),queryKeyword,queryKeyword,5000,queryKeyword, queryKeyword);

        List<RootApiResponseDto.FacilitiesInfoKeywordDto> facilitiesInfoDtoList = facilitiesList.stream()
                .map(facilities -> RootConverter.toFacilitiesInfoKeywordDto(facilities)).collect(Collectors.toList());

        return facilitiesInfoDtoList;
    }

    @Override
    @Transactional
    public String saveAsImageUrl(RootRequestDto.SaveImageAsUrlDto request) throws IOException
    {

            request.getImage().stream()
                    .map(image->{
                        Facilities facilities = null;
                        try {
                            String[] split = image.getOriginalFilename().split("\\.");
                            String name = null;
                            if(split.length > 0)
                                name = split[0];
                            facilities = facilitiesRepository.findById(Long.parseLong(name)).orElseThrow(() -> new RootException(Code.NOT_FOUND_FACILITY));
                            Uuid uuid = amazonS3Manager.createUUID();
                            String KeyName = amazonS3Manager.generateArticleKeyName(uuid, image.getOriginalFilename());
                            String fileUrl = amazonS3Manager.uploadFile(KeyName, image);
                            facilities.setImage(fileUrl);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        return facilities;
                    }).collect(Collectors.toList());

        return "hi!";
    }

    @Override
    @Transactional
    public Integer test() {

        List<Facilities> all = facilitiesRepository.findAll();

        List<String> keywords = new ArrayList<>();

        keywords.add("테니스");
        keywords.add("크로스핏");
        keywords.add("폴댄스");
        keywords.add("수영");
        keywords.add("스케이트");
        keywords.add("클라이밍");

        Integer totalUpdated = 0;

        for(String keyword : keywords){

            Integer page = 1;
            KakaoLocalResultDto.FacilityInfoListDto facilityInfoListDto = null;
            do {
                KakaoLocalParam build = KakaoLocalParam.builder()
                        .query(keyword)
                        .page(page)
                        .x(centerX)
                        .y(centerY)
                        .radius(radius)
                        .build();

                KakaoLocalResponseDto kakaoLocalFeignLocalInfo = kakaoLocalFeign.getLocalInfo(kakaoLocalApiKey, build);
                facilityInfoListDto = KakaoLocalConverter.toFacilityInfoListDto(kakaoLocalFeignLocalInfo);

                List<Facilities> facilitiesList = facilityInfoListDto.getFacilityInfoDtoList().stream()
                        .map(facilityInfoDto -> {
                                for(int i = 0; i < all.size(); i++){
                                    if(all.get(i).getName().equals(facilityInfoDto.getName())) {
                                        all.get(i).setKakaoId(facilityInfoDto.getKakaoId());
                                        return all.get(i);
                                    }
                                }
                                return null;
                                }
                        ).filter(Objects::nonNull).collect(Collectors.toList());

                totalUpdated += facilitiesList.size();
                page++;
            }while (!facilityInfoListDto.getIsEnd());
        }

        return totalUpdated;
    }

    @Override
    @Transactional
    public User changePermit(User user, RootRequestDto.NotificationChangeDto request) {
        return user.setPermit(request.getMarketingPermit(), request.getCommunityPermit());
    }

    @Override
    public List<RecommendFacilitiesKeyword>getRecommend() {
        return recommendFacilitiesKeywordRepository.findTop10ByOrderById();
    }

    @Override
    public List<Terms> getTerms() {
        List<Terms> repositoryAll = termsRepository.findAll();
        return repositoryAll;
    }

    @Override
    public Terms getTermsOne(Integer termsId) {
        return termsRepository.findById(termsId).get();
    }
}
