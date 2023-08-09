package fithub.app.feign.kakaoLocal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLocalMetaDto {
    Integer total_count;
    Integer pageable_count;
    Boolean is_end;
    KakaoLocalMetaSameNameDto same_name;
}
