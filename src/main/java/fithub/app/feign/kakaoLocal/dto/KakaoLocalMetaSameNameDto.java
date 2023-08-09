package fithub.app.feign.kakaoLocal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoLocalMetaSameNameDto {
    List<String> region;
    String keyword;
    String selected_region;
}
