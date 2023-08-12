package fithub.app.feign.kakaoLocal;

import fithub.app.feign.kakaoLocal.dto.KakaoLocalDocumentsDto;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalMetaDto;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResponseDto;
import fithub.app.feign.kakaoLocal.dto.KakaoLocalResultDto;

import java.util.List;
import java.util.stream.Collectors;

public class KakaoLocalConverter {

    public static KakaoLocalResultDto.FacilityInfoDto toFacilityInfoDto(KakaoLocalDocumentsDto kakaoLocalDocumentsDto){
        return KakaoLocalResultDto.FacilityInfoDto.builder()
                .name(kakaoLocalDocumentsDto.getPlace_name())
                .address(kakaoLocalDocumentsDto.getAddress_name())
                .roadAddress(kakaoLocalDocumentsDto.getRoad_address_name())
                .phoneNum(kakaoLocalDocumentsDto.getPhone())
                .X(kakaoLocalDocumentsDto.getX())
                .kakaoId(kakaoLocalDocumentsDto.getId())
                .Y(kakaoLocalDocumentsDto.getY())
                .build();
    }

    public static KakaoLocalResultDto.FacilityInfoListDto toFacilityInfoListDto(KakaoLocalResponseDto kakaoLocalResponseDto){

        List<KakaoLocalResultDto.FacilityInfoDto> facilityInfoDtoList = kakaoLocalResponseDto.getDocuments().stream()
                .map(kakaoLocalDocumentsDto -> toFacilityInfoDto(kakaoLocalDocumentsDto)).collect(Collectors.toList());

        return KakaoLocalResultDto.FacilityInfoListDto.builder()
                .facilityInfoDtoList(facilityInfoDtoList)
                .size(facilityInfoDtoList.size())
                .isEnd(kakaoLocalResponseDto.getMeta().getIs_end())
                .build();
    }
}
