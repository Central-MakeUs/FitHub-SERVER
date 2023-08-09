package fithub.app.feign.kakaoLocal.dto;

import lombok.*;

import java.util.List;

public class KakaoLocalResultDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilityInfoDto{
        private String name;
        private String address;
        private String roadAddress;
        private String phoneNum;
        private String Y;
        private String X;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FacilityInfoListDto{
        List<FacilityInfoDto> facilityInfoDtoList;
        Boolean isEnd;
        Integer size;
    }
}
