package fithub.app.service.impl;

import fithub.app.domain.ExerciseCategory;
import fithub.app.domain.Facilities;
import fithub.app.feign.kakaoLocal.KakaoLocalConverter;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalParam;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResponseDto;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResultDto;
import fithub.app.feign.kakaoLocal.service.KakaoLocalFeign;
import fithub.app.repository.ExerciseCategoryRepository;
import fithub.app.repository.FacilitiesRepository;
import fithub.app.service.KakaoLocalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoServiceImpl implements KakaoLocalService {

    private final KakaoLocalFeign kakaoLocalFeign;

    private final FacilitiesRepository facilitiesRepository;

    private final ExerciseCategoryRepository exerciseCategoryRepository;

    @Value("${kakao.localApiKey}")
    private String kakaoLocalApiKey;

    @Value("${kakao.centerX}")
    private String centerX;

    @Value("${kakao.centerY}")
    private String centerY;

    @Value("${kakao.radius}")
    private Integer radius;

    @Override
    @Transactional
    public Integer saveFacilities(String keyword, Integer categoryId) {

        Integer page = 1;
        Integer totalSaved = 0;
        KakaoLocalResultDto.FacilityInfoListDto facilityInfoListDto = null;

        ExerciseCategory exerciseCategory = exerciseCategoryRepository.findById(categoryId).get();
        do {

            KakaoLocalResponseDto kakaoLocalFeignLocalInfo = kakaoLocalFeign.getLocalInfo(kakaoLocalApiKey, KakaoLocalParam.builder()
                    .query(keyword)
                    .page(page)
                    .x(centerX)
                    .y(centerY)
                    .radius(radius)
                    .build());
            facilityInfoListDto = KakaoLocalConverter.toFacilityInfoListDto(kakaoLocalFeignLocalInfo);

            List<Facilities> facilitiesList = facilityInfoListDto.getFacilityInfoDtoList().stream()
                    .map(facilityInfoDto -> {

                        Optional<Facilities> byKakaoId = facilitiesRepository.findByKakaoId(facilityInfoDto.getKakaoId());

                        if (byKakaoId.isEmpty())
                            return facilitiesRepository.save(
                                    Facilities.builder()
                                            .phoneNum(facilityInfoDto.getPhoneNum())
                                            .roadAddress(facilityInfoDto.getRoadAddress())
                                            .address(facilityInfoDto.getAddress())
                                            .name(facilityInfoDto.getName())
                                            .kakaoId(facilityInfoDto.getKakaoId())
                                            .x(facilityInfoDto.getX())
                                            .y(facilityInfoDto.getY())
                                            .exerciseCategory(exerciseCategory)
                                            .build()
                            );
                        else
                            return null;
                    }

                    ).filter(Objects::nonNull).collect(Collectors.toList());

            totalSaved += facilitiesList.size();
            page++;
        }while (!facilityInfoListDto.getIsEnd());
        return totalSaved;
    }
}
