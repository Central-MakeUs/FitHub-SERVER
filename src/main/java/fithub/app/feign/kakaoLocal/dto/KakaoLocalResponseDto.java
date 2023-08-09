package fithub.app.feign.kakaoLocal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLocalResponseDto {
    KakaoLocalMetaDto meta;
    List<KakaoLocalDocumentsDto> documents;
}
